"use client";
import { useEffect, useState } from "react";
import { PieChart, Pie, Sector } from "recharts";
import "./RoomBookingChart.css";
import { bookings, organizations } from "@/typings";
import bookingService from "@/services/myRoom/booking/bookingService";

const renderActiveShape = (props: any) => {
  const RADIAN = Math.PI / 180;
  const {
    cx,
    cy,
    midAngle,
    innerRadius,
    outerRadius,
    startAngle,
    endAngle,
    fill,
    payload,
    percent,
    value,
  } = props;
  const sin = Math.sin(-RADIAN * midAngle);
  const cos = Math.cos(-RADIAN * midAngle);
  const sx = cx + (outerRadius + 10) * cos;
  const sy = cy + (outerRadius + 10) * sin;
  const mx = cx + (outerRadius + 30) * cos;
  const my = cy + (outerRadius + 30) * sin;
  const ex = mx + (cos >= 0 ? 1 : -1) * 22;
  const ey = my;
  const textAnchor = cos >= 0 ? "start" : "end";

  return (
    <g>
      <text x={cx} y={cy} dy={8} textAnchor="middle" fill={fill}>
        {payload.name}
      </text>
      <Sector
        cx={cx}
        cy={cy}
        innerRadius={innerRadius}
        outerRadius={outerRadius}
        startAngle={startAngle}
        endAngle={endAngle}
        fill={fill}
      />
      <Sector
        cx={cx}
        cy={cy}
        startAngle={startAngle}
        endAngle={endAngle}
        innerRadius={outerRadius + 6}
        outerRadius={outerRadius + 10}
        fill={fill}
      />
      <path
        d={`M${sx},${sy}L${mx},${my}L${ex},${ey}`}
        stroke={fill}
        fill="none"
      />
      <circle cx={ex} cy={ey} r={2} fill={fill} stroke="none" />
      <text
        x={ex + (cos >= 0 ? 1 : -1) * 12}
        y={ey}
        textAnchor={textAnchor}
        fill="#333"
      >{`${value} Bookings`}</text>
      <text
        x={ex + (cos >= 0 ? 1 : -1) * 12}
        y={ey}
        dy={18}
        textAnchor={textAnchor}
        fill="#999"
      >
        {`${payload.payload.name} People`}
      </text>
    </g>
  );
};

export default function RoomBookingChart({
  organization,
  error,
}: {
  organization: organizations.IOrganization;
  error: (message: string) => void;
}) {
  const [data, setDate] = useState<any | null>(null);

  useEffect(() => {
    const query: bookings.IGetPeopleCountsQuery = {
      organizationId: organization.id,
    };

    bookingService
      .getGetPeopleCounts(query)
      .then((data) => {
        const response: bookings.IGetPeopleCountsQueryData = data.data;
        setDate([
          { name: "Single", value: response.singleCounts },
          { name: "Double", value: response.doubleCounts },
          { name: "Other", value: response.othersCounts },
        ]);
      })
      .catch((err) => {
        console.log(err);
        error(err.message);
      });
  }, []);

  const [state, setState] = useState({
    activeIndex: 0,
  });

  return (
    data && (
      <div className="roomBookingChart">
        <div className="header">Room Usage Snapshot</div>
        <div>
          <PieChart width={420} height={250}>
            <Pie
              activeIndex={state.activeIndex}
              activeShape={renderActiveShape}
              data={data}
              cx="50%"
              cy="50%"
              innerRadius={60}
              outerRadius={80}
              fill="#a109ff"
              dataKey="value"
              onMouseEnter={(_, index) => {
                setState({
                  activeIndex: index,
                });
              }}
            />
          </PieChart>
        </div>
      </div>
    )
  );
}

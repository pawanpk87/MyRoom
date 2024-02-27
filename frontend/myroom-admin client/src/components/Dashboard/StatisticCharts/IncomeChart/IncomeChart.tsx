"use client";
import { XAxis, YAxis, Tooltip, Area, AreaChart, Legend } from "recharts";
import { Select } from "antd";
import { useEffect, useState } from "react";
import "./IncomeChart.css";
import { organizations, payments } from "@/typings";
import paymentService from "@/services/myRoom/payment/paymentService";
import { getWeekDayList } from "@/utils/utils";

export default function IncomeChart({
  organization,
  error,
}: {
  organization: organizations.IOrganization;
  error: (message: string) => void;
}) {
  const [dataType, setDataType] = useState<string>("Week");
  const [data, setData] = useState<any | null>(null);

  useEffect(() => {
    if (dataType === "Week") {
      const query: payments.IGetRevenueStatisticQuery = {
        organizationId: organization.id,
        duration: "week",
      };

      paymentService
        .getRevenuStatistic(query)
        .then((data) => {
          const statisticsData: payments.IGetRevenueStatisticData = data.data;
          const currData = statisticsData.curr;
          const preData = statisticsData.prev;

          const chartData = [];

          const days: string[] = getWeekDayList();

          for (let i = 0; i < preData.length; i++) {
            const curr = i < currData.length ? currData[i] : 0;
            const prev = preData[i];

            chartData.push({
              name: days[i],
              Current: curr,
              Prevoius: prev,
            });
          }

          setData(chartData);
        })
        .catch((err) => {
          console.log(error);
          error(err.message);
        });
    }
  }, [dataType]);

  return (
    <div className="incomeChartDiv">
      <div className="header">
        <div>
          <h3>Revenue</h3>
        </div>
        <Menu dataType={dataType} setDataType={setDataType} />
      </div>
      <div>
        <AreaChart
          width={750}
          height={425}
          data={data}
          margin={{ top: 10, right: 30, left: 0, bottom: 10 }}
        >
          <defs>
            <linearGradient id="colorUv" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#008000" stopOpacity={0.8} />
              <stop offset="95%" stopColor="#008000" stopOpacity={0} />
            </linearGradient>
            <linearGradient id="colorPv" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#0000ff" stopOpacity={0.8} />
              <stop offset="95%" stopColor="#0000ff" stopOpacity={0} />
            </linearGradient>
          </defs>

          <XAxis dataKey="name" />
          <YAxis />

          <Tooltip />

          <Legend />

          <Area
            type="monotone"
            dataKey="Current"
            stroke="#008000"
            dot={false}
            fillOpacity={1}
            fill="url(#colorUv)"
          />

          <Area
            type="monotone"
            dataKey="Prevoius"
            stroke="#0000ff"
            dot={false}
            fillOpacity={1}
            fill="url(#colorPv)"
          />
        </AreaChart>
      </div>
    </div>
  );
}

function Menu({
  dataType,
  setDataType,
}: {
  dataType: string;
  setDataType: React.Dispatch<React.SetStateAction<string>>;
}): JSX.Element {
  const handleChange = (value: string) => {
    setDataType(value);
  };

  return (
    <Select
      defaultValue="Week"
      style={{ width: 120 }}
      onChange={handleChange}
      options={[{ value: "Week", label: "Week" }]}
    />
  );
}

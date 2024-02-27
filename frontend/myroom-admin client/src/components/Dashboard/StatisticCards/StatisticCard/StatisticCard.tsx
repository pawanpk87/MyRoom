import {
  BarChart,
  Bar,
  Tooltip,
  LineChart,
  Line,
  CartesianGrid,
} from "recharts";
import "./StatisticCard.css";

export enum StatisticCardChartType {
  BAR,
  LINE,
}

export interface StatisticCardProps {
  title: string;
  key: string;
  total: number;
  thisMonth: number;
  thisWeek: number;
  data: any;
  chartType: StatisticCardChartType;
}

export default function StatisticCard({ data }: { data: StatisticCardProps }) {
  return (
    data && (
      <div className="statisticCard">
        <div className="statisticCardHeader">
          <div className="title">{data.title}</div>
          <div className="numbers">
            <span>{data.total}</span>
            <span></span>
          </div>
        </div>
        <div className="statisticCardNumbers">
          <div className="numbers">
            <Numbers title="THIS MONTH" number={data.thisMonth} />
            <Numbers title="THIS WEEK" number={data.thisWeek} />
          </div>
          <div className="chart">
            {data.chartType === StatisticCardChartType.BAR ? (
              <CustomBarChart data={data} />
            ) : (
              <CustomLineChart data={data.data} />
            )}
          </div>
        </div>
      </div>
    )
  );
}

function Numbers({
  title,
  number,
}: {
  title: string;
  number: number;
}): JSX.Element {
  return (
    <div className="numberCard">
      <span className="title">{title}</span>
      <span>{number}</span>
    </div>
  );
}

function CustomBarChart({ data }: { data: StatisticCardProps }): JSX.Element {
  return (
    <div>
      <BarChart
        width={150}
        height={40}
        data={data.data}
        defaultShowTooltip={false}
      >
        {/* <CartesianGrid strokeDasharray="1 1" /> */}
        <Bar dataKey={data.key} fill="#816bff" />
        <Tooltip
          cursor={false}
          contentStyle={{
            borderRadius: "10px",
            fontSize: "16px",
          }}
        />
      </BarChart>
    </div>
  );
}

function CustomLineChart({ data }: { data: any }): JSX.Element {
  return (
    <LineChart width={160} height={40} data={data}>
      <Tooltip />
      <Line type="monotone" dataKey="pv" stroke="#8884d8" dot={false} />
      <Line type="monotone" dataKey="uv" stroke="#82ca9d" dot={false} />
    </LineChart>
  );
}

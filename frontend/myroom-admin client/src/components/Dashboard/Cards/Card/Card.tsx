import "./Card.css";

export interface DashboardCardProps {
  title: string;
  number: number;
  icon: any;
}

export default function Card({ data }: { data: DashboardCardProps }) {
  return (
    <div className="dashboardCard">
      <div className="content">
        <div className="">
          <strong>{data.number}</strong>
        </div>
        <div>{data.title}</div>
      </div>
      <div className="icon">{data.icon}</div>
    </div>
  );
}

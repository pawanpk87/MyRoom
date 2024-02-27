import { Result } from "antd";
import Link from "next/link";

export default function NotFound() {
  return (
    <Result
      status="404"
      title="404"
      subTitle="Bokking does not exist."
      extra={<Link href="/">Return Home</Link>}
    />
  );
}

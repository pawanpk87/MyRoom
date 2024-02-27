import Image from "next/image";
import Link from "next/link";

export default function Home() {
  return (
    <div className="dashboardMain">
      <div className="tagDetails">
        <div className="tag">
          <h1>MyRoom: Your Premier Booking Platform for Seamless Growth</h1>
        </div>

        <div className="desc">
          Experience the power of myRoom: the ultimate booking platform for
          unleashing your business potential. Join now and watch your space
          thrive.
        </div>

        <Link href={`/login `} className="getDhashboardBtn">
          Get Dhasboard
        </Link>
      </div>
      <div className="dashboardImg">
        <Image
          src={"/images/myroomdashboard.png"}
          alt={"login"}
          height={1000}
          width={1000}
          style={{
            width: "90%",
            height: "65vh",
            borderRadius: "20px",
          }}
        />
      </div>
    </div>
  );
}

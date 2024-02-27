import authService from "@/services/myRoom/authService/authService";
import { auth } from "@/typings";
import { Avatar } from "antd";
import Image from "next/image";
import React, { useEffect } from "react";

export default function User({ uid }: { uid: string }) {
  const [user, setUser] = React.useState<auth.IUser | null>(null);

  useEffect(() => {
    authService
      .getUser(uid)
      .then((data) => {
        setUser(data.data);
      })
      .catch((error: any) => {
        console.log(error.message);
      });
  }, [uid]);

  return (
    user && (
      <div
        style={{
          display: "flex",
          flexDirection: "row",
          gap: "5px",
          fontSize: "10px",
        }}
      >
        <Avatar
          src={
            <Image src={user.picture} alt={user.name} height={20} width={20} />
          }
        />

        <div
          style={{
            display: "flex",
            flexDirection: "column",
          }}
        >
          <span>{user.email}</span>
          <span>{user.name}</span>
        </div>
      </div>
    )
  );
}

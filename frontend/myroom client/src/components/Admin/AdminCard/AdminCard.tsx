"use client";
import { Avatar } from "antd";
import "./AdminCard.css";
import React, { useEffect } from "react";
import authService from "@/services/myRoom/auth/authService";
import { auth } from "@/typings";
import Image from "next/image";

export default function AdminCard({
  uid,
  organizationId,
}: {
  uid: string;
  organizationId: string;
}) {
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
      <div className="admin">
        <div className="img">
          <Avatar
            src={
              <Image
                src={user.picture}
                alt={user.name}
                height={20}
                width={20}
              />
            }
          />
        </div>

        <div className="admdetails">
          <div className="name">
            <span>Hosted By</span>
          </div>

          <div className="list">
            <span>{user.name}</span>
          </div>
        </div>
      </div>
    )
  );
}

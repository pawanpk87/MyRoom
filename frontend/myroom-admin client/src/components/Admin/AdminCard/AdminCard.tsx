"use client";
import { Avatar, Badge, Tag } from "antd";
import { useEffect, useState } from "react";
import { auth, organizations } from "@/typings";
import authService from "@/services/myRoom/authService/authService";
import "./AdminCard.css";

export default function AdminCard({
  id,
  type,
}: {
  id: string;
  type: organizations.IAdminType;
}) {
  const [user, setUser] = useState<auth.IUser | null>(null);

  useEffect(() => {
    authService
      .getUser(id)
      .then((data) => {
        setUser(data.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [id]);

  return (
    <div className="admin">
      <div className="img">
        <Avatar src={user?.picture} />
      </div>
      <div className="admdetails">
        <div className="name">
          <span>{user?.name}</span>
        </div>
        <div className="list">
          {type === organizations.IAdminType.SUPER_ADMIN ? (
            <Tag color="#87d068">Super Admin</Tag>
          ) : (
            <Tag>Admin</Tag>
          )}
        </div>
      </div>
    </div>
  );
}

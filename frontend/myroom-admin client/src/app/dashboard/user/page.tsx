"use client";
import { useUserAuth } from "@/firebase/auth/authProvider";
import { Button } from "antd";
import { deleteCookie } from "cookies-next";
import React, { useState } from "react";
import Image from "next/image";
import "./user.css";

export default function User() {
  const { user, logOut } = useUserAuth();
  const [loading, setLoading] = useState<boolean>(false);

  const handleSignOut = async () => {
    setLoading(true);
    try {
      await logOut();
      deleteCookie("userData");
      setLoading(false);
      location.reload();
    } catch (error) {
      setLoading(false);
      location.reload();
      console.log(error);
    }
  };

  return (
    user && (
      <div className="user">
        <div className="image">
          <Image
            src={"/images/admin.jpg"}
            alt={"admin"}
            height={650}
            width={650}
          />
        </div>

        <div className="userDetails">
          <div className="image">
            <Image
              src={user.photoURL}
              alt={user.displayName}
              height={400}
              width={400}
              style={{ borderRadius: "20px" }}
            />
          </div>
          <div>
            <h1>{user.email}</h1>
            <h1>{user.displayName}</h1>
          </div>
        </div>

        <br />

        {user && (
          <Button type="primary" loading={loading} onClick={handleSignOut}>
            Log Out
          </Button>
        )}
      </div>
    )
  );
}

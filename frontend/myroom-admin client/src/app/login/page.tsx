"use client";
import { Button } from "antd";
import { useUserAuth } from "@/firebase/auth/authProvider";
import React, { useEffect, useState } from "react";
import { deleteCookie } from "cookies-next";
import { useRouter } from "next/navigation";
import Image from "next/image";
import "./login.css";

export default function Login() {
  const router = useRouter();
  const { user, googleSignIn, logOut } = useUserAuth();
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

  useEffect(() => {
    if (user) {
      router.push("/onboarding");
    }
  }, [user]);

  return (
    <div className="login">
      <div>
        <Image
          src={"/images/login.jpg"}
          alt={"login"}
          height={700}
          width={700}
        />
      </div>
      <div className="loginScreen">
        <div className="header">
          <h1>SignIn</h1>
        </div>
        {user === null && (
          <Button
            type="primary"
            onClick={() => {
              googleSignIn();
            }}
          >
            Log In
          </Button>
        )}

        <br />
        <br />
        <br />

        {user && (
          <Button type="primary" loading={loading} onClick={handleSignOut}>
            Log Out
          </Button>
        )}
      </div>
    </div>
  );
}

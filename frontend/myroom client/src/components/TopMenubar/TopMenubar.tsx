"use client";
import React from "react";
import { UserOutlined } from "@ant-design/icons";
import { Avatar } from "antd";
import { Menu, MenuButton, Button, MenuItem, MenuList } from "@chakra-ui/react";
import { ArrowForwardIcon } from "@chakra-ui/icons";
import Image from "next/image";
import Searchbox from "./Searchbox";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useUserAuth } from "@/firebase/auth/authProvider";
import { deleteCookie } from "cookies-next";
import "./TopMenubar.css";

function TopMenubar() {
  const pathname = usePathname();
  const router = useRouter();

  const { user, googleSignIn, logOut } = useUserAuth();
  const [loading, setLoading] = React.useState<boolean>(false);

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
    <div className="header">
      <div className="logo">
        <Link href={"/"}>
          <Image
            src="/favicon.ico"
            alt="myRoom Logo"
            width={80}
            height={70}
            style={{ width: "50px", height: "50px" }}
          />
        </Link>
      </div>

      {pathname && pathname === "/search" && <Searchbox />}

      <div className="menu">
        <Button
          rightIcon={<ArrowForwardIcon />}
          colorScheme="blue"
          variant="outline"
          height={"50px"}
          onClick={() => {
            setLoading(true);
            window.location.href = "http://localhost:3001/";
          }}
        >
          List your property
        </Button>

        {user ? (
          <Menu>
            <MenuButton
              as={Button}
              height={"50px"}
              rightIcon={
                <Avatar
                  style={{ backgroundColor: "#87d068" }}
                  icon={<UserOutlined />}
                />
              }
            >
              {user.displayName?.substring(0, 5)}...
            </MenuButton>
            <MenuList>
              <MenuItem onClick={() => router.push("/bookings")}>
                My Bookings
              </MenuItem>
              <MenuItem onClick={handleSignOut}> Log out</MenuItem>
            </MenuList>
          </Menu>
        ) : (
          <Button
            colorScheme="messenger"
            height={"50px"}
            onClick={googleSignIn}
          >
            Login
          </Button>
        )}
      </div>
    </div>
  );
}

export default TopMenubar;

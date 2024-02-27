"use client";
import React, { useEffect, useState } from "react";
import {
  CalendarOutlined,
  DashboardOutlined,
  HomeOutlined,
  NotificationOutlined,
  StarOutlined,
  UserOutlined,
} from "@ant-design/icons";
import type { MenuProps } from "antd";
import { Avatar, Menu } from "antd";
import Image from "next/image";
import Link from "next/link";
import { useUserAuth } from "@/firebase/auth/authProvider";
import { usePathname } from "next/navigation";

type MenuItem = Required<MenuProps>["items"][number];

function getItem(
  label: React.ReactNode,
  key: React.Key | null,
  icon?: React.ReactNode,
  children?: MenuItem[],
  type?: "group"
): MenuItem {
  return {
    key,
    icon,
    children,
    label,
    type,
  } as MenuItem;
}

function Sidebar() {
  const { user, organization } = useUserAuth();
  const [menuItems, setMenuItems] = useState<any[]>([]);

  useEffect(() => {
    if (user && organization) {
      const items: MenuProps["items"] = [
        getItem(
          <Link href="/">{organization.name}</Link>,
          "Logo",
          <Image
            src="/favicon.ico"
            alt="MyRoom Admin"
            width={60}
            height={55}
            style={{ width: "auto", height: "auto" }}
          />
        ),

        { type: "divider" },

        getItem(
          <Link href="/dashboard">Dashboard</Link>,
          "dashboard",
          <DashboardOutlined />
        ),

        getItem(
          <Link href="/dashboard/rooms">Rooms</Link>,
          "rooms",
          <HomeOutlined />
        ),

        getItem(
          <Link href="/dashboard/bookings">Bookings</Link>,
          "bookings",
          <CalendarOutlined />
        ),

        getItem(
          <Link href="/dashboard/reviews">Reviews</Link>,
          "reviews",
          <StarOutlined />
        ),

        { type: "divider" },

        getItem(
          <Link href="/dashboard/organization">{organization.name}</Link>,
          "organization",
          <HomeOutlined />
        ),

        getItem(
          <Link href="/dashboard/settings/admins">Admins</Link>,
          "admins",
          <UserOutlined />
        ),

        getItem(
          <Link href="/dashboard/notifications">Notifications</Link>,
          "notifications",
          <NotificationOutlined />
        ),

        { type: "divider" },

        getItem(
          <Link href="/dashboard/user">{user.displayName!}</Link>,
          user.displayName!,
          <Avatar
            src={
              <Image
                src={user.photoURL!}
                alt={user.displayName!}
                height={20}
                width={20}
              />
            }
          />
        ),
      ];
      setMenuItems([...items]);
    }
  }, [user, organization]);

  return (
    <Menu
      style={{
        width: "300px",
        height: "100vh",
        overflow: "scroll",
        borderRight: "1px solid #dfdbdb",
      }}
      mode="inline"
      items={menuItems}
    />
  );
}

export default Sidebar;

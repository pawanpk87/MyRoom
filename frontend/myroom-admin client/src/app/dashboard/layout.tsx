"use client";
import React from "react";
import "./dashboard.css";

import Sidebar from "@/components/Sidebar/Sidebar";

export default function DashboardRootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <section className="myroom-admin-main">
      <Sidebar />
      {children}
    </section>
  );
}

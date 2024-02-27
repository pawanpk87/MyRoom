"use client";
import type { Metadata } from "next";
import React from "react";
import "./page.css";
import "./globals.css";
import { UserAuthProvider } from "@/firebase/auth/authProvider";

// export const metadata: Metadata = {
//   title: "MyRoom Admin",
//   description: "MyRoom Admin",
// };

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className="myroom-admin-main">
        <UserAuthProvider>{children}</UserAuthProvider>
      </body>
    </html>
  );
}

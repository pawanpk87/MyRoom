"use client";
import type { Metadata } from "next";
import "./globals.css";

// Chakra
import { ChakraProvider } from "@chakra-ui/react";

// Date range picker
import "react-date-range/dist/styles.css"; // main css file
import "react-date-range/dist/theme/default.css"; // theme css file

import TopMenubar from "@/components/TopMenubar/TopMenubar";
import { UserAuthProvider } from "@/firebase/auth/authProvider";
import { GithubOutlined, LinkedinOutlined } from "@ant-design/icons";
import Link from "next/link";

// export const metadata: Metadata = {
//   title: "MyRoom",
//   description: "MyRoom",
// };

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <UserAuthProvider>
          <ChakraProvider>
            <TopMenubar />

            {/* <div className="githublink">
              <div className="icon">
                <Link
                  href={"https://github.com/pawanpk87/myShop-Ecommerce-website"}
                >
                  <GithubOutlined style={{ fontSize: "30px" }} />
                </Link>
              </div>

              <div className="icon">
                <Link href={"https://www.linkedin.com/in/pawan-kumar-mehta-/"}>
                  <LinkedinOutlined style={{ fontSize: "30px" }} />
                </Link>
              </div>
            </div> */}

            {children}
          </ChakraProvider>
        </UserAuthProvider>
      </body>
    </html>
  );
}

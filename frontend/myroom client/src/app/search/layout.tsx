"use client";
import React, { Suspense } from "react";
import { MagnifyingGlass } from "react-loader-spinner";
import SearchSidebar from "@/components/Search/SearchSidebar/SearchSidebar";
import "./search.css";

export default function SearchLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="searchRoom">
      <SearchSidebar />
      <Suspense
        fallback={
          <div id="searchLoader">
            <MagnifyingGlass
              visible={true}
              height="80"
              width="80"
              ariaLabel="magnifying-glass-loading"
              wrapperStyle={{}}
              wrapperClass="magnifying-glass-wrapper"
              glassColor="#c0efff"
              color="#e15b64"
            />
          </div>
        }
      >
        <section className="searchList">{children}</section>
      </Suspense>
    </div>
  );
}

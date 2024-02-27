"use client";
import { addValueToQueryString } from "@/utils/utils";
import { Pagination } from "antd";
import { usePathname, useRouter, useSearchParams } from "next/navigation";

export default function NavPagination({
  totalRecods,
  totalPages,
  currentPage,
}: {
  totalRecods: number;
  totalPages: number;
  currentPage: number;
}) {
  const pathname = usePathname();
  const router = useRouter();
  const searchParam = useSearchParams();

  const onPageChange = (page: number, pageSize: number) => {
    const query = addValueToQueryString(searchParam, "page", page.toString());
    router.push(pathname + "?" + query);
  };

  return (
    <Pagination
      className="pagination"
      defaultCurrent={1}
      onChange={onPageChange}
      total={totalRecods}
    />
  );
}

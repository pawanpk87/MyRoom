import { ReadonlyURLSearchParams } from "next/navigation";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
dayjs.extend(customParseFormat);

export function formatDayjsDate(date: Date | string, dateFormat: string) {
  const formatedDate: dayjs.Dayjs = dayjs(date, dateFormat);
  return formatedDate;
}

export function formatShortDate(date: Date): string {
  const options: any = { weekday: "short", day: "numeric", month: "short" };
  return date.toLocaleString("en-US", options);
}

export function addValueToQueryString(
  searchParam: ReadonlyURLSearchParams,
  name: string,
  value: string
) {
  const params = new URLSearchParams(searchParam);
  params.set(name, value);
  return params.toString();
}

export function addValuesToQueryString(
  searchParam: ReadonlyURLSearchParams,
  values: {
    key: string;
    value: string;
  }[]
) {
  const params = new URLSearchParams(searchParam);
  values.forEach((value) => {
    params.set(value.key, value.value);
  });
  return params.toString();
}

export function deleteKeyFromQueryString(
  searchParam: ReadonlyURLSearchParams,
  key: string
) {
  const params = new URLSearchParams(searchParam);
  if (params.has(key)) {
    params.delete(key);
  }
  return params.toString();
}

export function calculatePercentage(number: number, totalNumber: number) {
  return Number(((number / totalNumber) * 100).toFixed(0));
}

export interface SearchParams {
  cost?: {
    operator: string;
    cost: number;
  };
  city?: string;
  rating?: {
    rating: {
      operator: string;
      rating: number;
    };
  };
  amenities?: string[];
  features?: string[];
}

export function updateSearchParams(
  searchParams: URLSearchParams,
  updates: Partial<SearchParams>
): string {
  const currentParams: SearchParams = {};

  searchParams.forEach((value, key) => {
    if (key === "cost") {
      currentParams.cost = JSON.parse(decodeURIComponent(value));
    } else if (key === "city") {
      currentParams.city = value;
    } else if (key === "rating") {
      currentParams.rating = JSON.parse(decodeURIComponent(value));
    } else if (key === "amenities") {
      currentParams.amenities = JSON.parse(decodeURIComponent(value));
    } else if (key === "features") {
      currentParams.features = JSON.parse(decodeURIComponent(value));
    }
  });

  const updatedParams: SearchParams = { ...currentParams, ...updates };

  const updatedSearch = new URLSearchParams();

  for (const [key, value] of Object.entries(updatedParams)) {
    if (key === "city") {
      updatedSearch.append(key, value);
    } else {
      updatedSearch.append(key, JSON.stringify(value));
    }
  }

  return updatedSearch.toString();
}

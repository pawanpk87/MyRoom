"use client";
import React, { useEffect, useState } from "react";
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";
import {
  ReadonlyURLSearchParams,
  usePathname,
  useRouter,
  useSearchParams,
} from "next/navigation";
import { Checkbox, Select, SelectProps, Slider } from "antd";
import type { CheckboxChangeEvent } from "antd/es/checkbox";
import "./SearchSidebar.css";
import { rooms } from "@/typings";
import {
  addValueToQueryString,
  deleteKeyFromQueryString,
  updateSearchParams,
} from "@/utils/utils";
import { DefaultOptionType } from "antd/es/select";

function SearchSidebar() {
  const pathname = usePathname();
  const router = useRouter();
  const searchParam = useSearchParams();

  return (
    <aside className="sidebar">
      <div className="content">
        <Header pathname={pathname} router={router} searchParam={searchParam} />

        <Price pathname={pathname} router={router} searchParam={searchParam} />

        <Amenities
          pathname={pathname}
          router={router}
          searchParam={searchParam}
        />

        <Features
          pathname={pathname}
          router={router}
          searchParam={searchParam}
        />

        {/* <CheckInFeatures
          pathname={pathname}
          router={router}
          searchParam={searchParam}
        /> */}

        <Rating pathname={pathname} router={router} searchParam={searchParam} />
      </div>
    </aside>
  );
}

function Header({
  pathname,
  router,
  searchParam,
}: {
  pathname: string;
  router: AppRouterInstance;
  searchParam: ReadonlyURLSearchParams;
}) {
  return (
    <div className="search-sidebar-header">
      <div>
        <span>Filters</span>
      </div>
      <div>
        <span
          style={{ color: "blue", cursor: "pointer" }}
          onClick={() => {
            router.push("/search");
          }}
        >
          Clear All
        </span>
      </div>
    </div>
  );
}

function Price({
  pathname,
  router,
  searchParam,
}: {
  pathname: string;
  router: AppRouterInstance;
  searchParam: ReadonlyURLSearchParams;
}) {
  interface ICost {
    min: number;
    max: number;
  }

  const [cost, setCost] = React.useState<ICost>({
    min: 2000,
    max: 10000,
  });

  const [value, setValue] = useState<number | null>(null);

  useEffect(() => {
    if (searchParam.has("cost")) {
      let cost = JSON.parse(decodeURIComponent(searchParam.get("cost")!));
      setValue(cost.cost);
    } else {
      setValue(2000);
    }
  }, [searchParam]);

  return (
    <div className="filterCheckbox">
      <strong>Price</strong>
      {value && (
        <Slider
          min={1000}
          max={10000}
          defaultValue={value}
          onChange={(value) => {
            setCost({
              ...cost,
              min: value,
            });
          }}
          onChangeComplete={(value) => {
            const query = updateSearchParams(searchParam, {
              cost: {
                operator:
                  rooms.QueryRelationalOperatorEnum.GREATER_THAN_OR_EQUAL,
                cost: value,
              },
            });
            router.push(pathname + "?" + query);
          }}
        />
      )}

      <div
        style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
          gap: "10px",

          fontWeight: "bold",
        }}
      >
        <span>₹ {cost.min}</span>

        <span>₹ {cost.max}</span>
      </div>
    </div>
  );
}

function Rating({
  pathname,
  router,
  searchParam,
}: {
  pathname: string;
  router: AppRouterInstance;
  searchParam: ReadonlyURLSearchParams;
}) {
  const onChange = (value: number | null) => {
    let query = {};
    if (value) {
      query = updateSearchParams(searchParam, {
        rating: {
          rating: {
            operator: rooms.QueryRelationalOperatorEnum.GREATER_THAN_OR_EQUAL,
            rating: value,
          },
        },
      });
    } else {
      query = deleteKeyFromQueryString(searchParam, "rating");
    }
    router.push(`${pathname === "/" ? "/search" : pathname}?${query}`);
  };

  const [checkedRating, setCheckedRating] = useState<number | null>(null);

  const handleCheckboxChange = (rating: number) => {
    if (checkedRating === rating) {
      setCheckedRating(null);
      onChange(null);
    } else {
      setCheckedRating(rating);
      onChange(rating);
    }
  };

  useEffect(() => {
    if (searchParam.has("rating")) {
      let rating = JSON.parse(decodeURIComponent(searchParam.get("rating")!));
      setCheckedRating(rating.rating.rating);
    }
  }, [searchParam]);

  return (
    <div className="filterCheckbox">
      <strong>Property rating</strong>
      <div>
        <Checkbox
          onChange={(e: CheckboxChangeEvent) => {
            handleCheckboxChange(5);
          }}
          checked={checkedRating === 5}
        >
          5★{" "}
        </Checkbox>
        <Checkbox
          onChange={(e: CheckboxChangeEvent) => {
            handleCheckboxChange(4);
          }}
          checked={checkedRating === 4}
        >
          4★ & above
        </Checkbox>
        <Checkbox
          onChange={(e: CheckboxChangeEvent) => {
            handleCheckboxChange(3);
          }}
          checked={checkedRating === 3}
        >
          3★ & above
        </Checkbox>
        <Checkbox
          onChange={(e: CheckboxChangeEvent) => {
            handleCheckboxChange(2);
          }}
          checked={checkedRating === 2}
        >
          2★ & above
        </Checkbox>
      </div>
    </div>
  );
}

function Amenities({
  pathname,
  router,
  searchParam,
}: {
  pathname: string;
  router: AppRouterInstance;
  searchParam: ReadonlyURLSearchParams;
}) {
  const options: SelectProps["options"] = [
    {
      label: "Free Wifi",
      value: "Free Wifi",
    },
    {
      label: "TV",
      value: "TV",
    },
    {
      label: "AC",
      value: "AC",
    },

    {
      label: "Geyser",
      value: "Geyser",
    },
    {
      label: "Air conditioning",
      value: "Air conditioning",
    },
    {
      label: "Washing machine",
      value: "Washing machine",
    },

    {
      label: "Heating",
      value: "Heating",
    },
    {
      label: "Dryer",
      value: "Dryer",
    },

    {
      label: "Elevator",
      value: "Elevator",
    },
  ];

  const [selectedValues, setSelectedValues] = React.useState<string[]>([]);

  const handleChange = (
    value: string[],
    option: DefaultOptionType | DefaultOptionType[]
  ) => {
    let query: string = "";
    if (value.length > 0) {
      query = updateSearchParams(searchParam, {
        amenities: value,
      });
    } else {
      query = deleteKeyFromQueryString(searchParam, "amenities");
    }

    router.push(`${pathname === "/" ? "/search" : pathname}?${query}`);

    setSelectedValues(value);
  };

  useEffect(() => {
    if (searchParam.has("amenities")) {
      let amenities = JSON.parse(
        decodeURIComponent(searchParam.get("amenities")!)
      );
      setSelectedValues(amenities);
    }
  }, [searchParam]);

  return (
    <div className="amenties">
      <strong>Amenities</strong>
      <div>
        <Select
          mode="multiple"
          allowClear
          style={{ width: "100%" }}
          placeholder="Please select"
          value={selectedValues}
          onChange={handleChange}
          options={options}
        />
      </div>
    </div>
  );
}

function Features({
  pathname,
  router,
  searchParam,
}: {
  pathname: string;
  router: AppRouterInstance;
  searchParam: ReadonlyURLSearchParams;
}) {
  const options: SelectProps["options"] = [
    {
      label: "Gym",
      value: "Gym",
    },
    {
      label: "Security",
      value: "Security",
    },
    {
      label: "Pool",
      value: "Pool",
    },

    {
      label: "Security cameras on property",
      value: "Security cameras on property",
    },
    {
      label: "Free parking",
      value: "Free parking",
    },
    {
      label: "Smoking allowed",
      value: "Smoking allowed",
    },

    {
      label: "Dedicated workspace",
      value: "Dedicated workspace",
    },
    {
      label: "Daily housekeeping",
      value: "Daily housekeeping",
    },

    {
      label: "Parking facility",
      value: "Parking facility",
    },

    {
      label: "Power backup",
      value: "Power backup",
    },

    {
      label: "Garden",
      value: "Garden",
    },

    {
      label: "Smoking allowed",
      value: "Smoking allowed",
    },
  ];

  const [selectedValues, setSelectedValues] = React.useState<string[]>([]);

  const handleChange = (
    value: string[],
    option: DefaultOptionType | DefaultOptionType[]
  ) => {
    let query: string = "";
    if (value.length > 0) {
      query = updateSearchParams(searchParam, { features: value });
    } else {
      query = deleteKeyFromQueryString(searchParam, "features");
    }

    router.push(pathname + "?" + query);

    setSelectedValues(value);
  };

  useEffect(() => {
    if (searchParam.has("features")) {
      let features = JSON.parse(searchParam.get("features"));
      setSelectedValues(features);
    }
  }, [searchParam]);

  return (
    <div className="features">
      <strong>Features</strong>
      <div>
        <Select
          mode="multiple"
          allowClear
          style={{ width: "100%" }}
          placeholder="Please select"
          value={selectedValues}
          onChange={handleChange}
          options={options}
        />
      </div>
    </div>
  );
}

function CheckInFeatures({
  pathname,
  router,
  searchParam,
}: {
  pathname: string;
  router: AppRouterInstance;
  searchParam: ReadonlyURLSearchParams;
}) {
  const [check, setCheck] = React.useState<boolean>(false);

  const onChange = (e: CheckboxChangeEvent) => {
    let query: string = "";
    if (e.target.checked) {
      query = addValueToQueryString(
        searchParam,
        "checkInType",
        rooms.CheckInType.PAY_AT_HOTEL
      );
    } else {
      query = deleteKeyFromQueryString(searchParam, "checkInType");
    }
    router.push(pathname + "?" + query);
    setCheck(e.target.checked);
  };

  useEffect(() => {
    if (searchParam.has("checkInType")) {
      if (searchParam.get("checkInType") === "PAY_AT_HOTEL") {
        setCheck(true);
      } else {
        setCheck(false);
      }
    }
  }, [searchParam]);

  return (
    <div className="price">
      <strong>Check-in features</strong>
      <div>
        <Checkbox checked={check} onChange={onChange}>
          Pay at Hotel
        </Checkbox>
      </div>
    </div>
  );
}

export default SearchSidebar;

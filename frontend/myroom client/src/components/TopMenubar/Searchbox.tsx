"use client";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import { addDays } from "date-fns";
import React, { useEffect, useState } from "react";
import {
  addValueToQueryString,
  addValuesToQueryString,
  formatDayjsDate,
} from "@/utils/utils";
import { DatePicker, Popover, Select } from "antd";
import dayjs from "dayjs";
import type { SelectProps } from "antd";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { bookings, geocoding, rooms } from "@/typings";
import locationService from "@/services/openWeatherMap/locationService";
import { useToast } from "@chakra-ui/react";
import Guests from "../Guests/Guests";
import { RangePickerProps } from "antd/es/date-picker";
const dateFormat = "YYYY-MM-DD";

function Searchbox() {
  const [dates, setDates] = useState<Date[]>([
    new Date(),
    addDays(new Date(), 1),
  ]);

  const [guestsList, setGuestsList] = React.useState<bookings.IGuestList>({
    adults: 1,
    children: 0,
  });

  return (
    <div className="search-box">
      <Location />

      <Dates dates={dates} setDates={setDates} />

      <GuestList guestsList={guestsList} setGuestsList={setGuestsList} />

      <Search dates={dates} guestsList={guestsList} />
    </div>
  );
}

function Location(): JSX.Element {
  const pathname = usePathname();
  const router = useRouter();
  const searchParam = useSearchParams();

  const toast = useToast();

  const [data, setData] = useState<SelectProps["options"]>([]);
  const [value, setValue] = useState<string | null>(searchParam.get("city"));

  const handleSearch = (newValue: string) => {
    if (newValue.length > 2) {
      locationService
        .getCities({
          q: newValue,
          limit: 5,
        })
        .then((cities) => {
          let allCities = cities.data.map((city: geocoding.ICity) => {
            return {
              value: city.name,
              text: `${city.name}${city.state ? "," + city.state : ""},${
                city.country
              }`,
            };
          });
          setData(allCities);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  const handleChange = (newValue: string) => {
    setValue(newValue);
  };

  const handleSelect = (selectedValue: string) => {
    const query = addValueToQueryString(searchParam, "city", selectedValue);
    router.push(`${pathname === "/" ? "/search" : pathname}?${query}`);
  };

  const onCurrentLocationHandle = async () => {
    const options = {
      enableHighAccuracy: true,
      maximumAge: 30000,
      timeout: 27000,
    };
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        locationService
          .getLocation({
            lat: pos.coords.latitude,
            lon: pos.coords.longitude,
          })
          .then((data) => {
            const cityName = data.data[0].name;
            setValue(cityName);
            handleSelect(cityName);
          })
          .catch((error) => {
            toast({
              title: `Error`,
              description: error.message,
              status: "error",
              isClosable: true,
            });
          });
      },
      (error) => {
        toast({
          title: `Error`,
          description: error.message,
          status: "error",
          isClosable: true,
        });
      },
      options
    );
  };

  useEffect(() => {
    if (searchParam.get("city")) {
      setValue(searchParam.get("city"));
    }
  }, [searchParam]);

  return (
    <div className="place">
      <Select
        showSearch
        autoClearSearchValue
        value={value}
        placeholder={"search"}
        style={{ width: "80%", height: "100%" }}
        bordered={false}
        defaultActiveFirstOption={false}
        suffixIcon={null}
        filterOption={false}
        onSearch={handleSearch}
        onChange={handleChange}
        onSelect={handleSelect}
        notFoundContent={null}
        options={(data || []).map((d) => ({
          value: d.value,
          label: d.text,
        }))}
      />
      <button onClick={onCurrentLocationHandle}>
        <LocationOnIcon />
      </button>
    </div>
  );
}

function Dates({
  dates,
  setDates,
}: {
  dates: Date[];
  setDates: React.Dispatch<React.SetStateAction<Date[]>>;
}): JSX.Element {
  const searchParam = useSearchParams();

  useEffect(() => {
    if (searchParam.get("availableDates")) {
      const searchParamDates = JSON.parse(searchParam.get("availableDates")!);
      setDates([
        new Date(searchParamDates.startDate.startDate),
        new Date(searchParamDates.endDate.endDate),
      ]);
    }
  }, [searchParam]);

  const range = (start: number, end: number) => {
    const result = [];
    for (let i = start; i < end; i++) {
      result.push(i);
    }
    return result;
  };

  const disabledDate: RangePickerProps["disabledDate"] = (current) => {
    return current && current <= dayjs().endOf("day");
  };

  const disabledDateTime = () => ({
    disabledHours: () => range(0, 24).splice(4, 20),
    disabledMinutes: () => range(30, 60),
    disabledSeconds: () => [55, 56],
  });

  const disabledRangeTime: RangePickerProps["disabledTime"] = (_, type) => {
    if (type === "start") {
      return {
        disabledHours: () => range(0, 60).splice(4, 20),
        disabledMinutes: () => range(30, 60),
        disabledSeconds: () => [55, 56],
      };
    }
    return {
      disabledHours: () => range(0, 60).splice(20, 4),
      disabledMinutes: () => range(0, 31),
      disabledSeconds: () => [55, 56],
    };
  };

  return (
    <DatePicker.RangePicker
      className="dates"
      disabledDate={disabledDate}
      disabledTime={disabledRangeTime}
      value={[
        formatDayjsDate(dates[0].toISOString(), dateFormat),
        formatDayjsDate(dates[1].toISOString(), dateFormat),
      ]}
      onChange={(values) => {
        if (values && values.length > 0) {
          setDates([values[0]!.toDate(), values[1]!.toDate()]);
        }
      }}
    />
  );
}

function GuestList({
  guestsList,
  setGuestsList,
}: {
  guestsList: bookings.IGuestList;
  setGuestsList: React.Dispatch<React.SetStateAction<bookings.IGuestList>>;
}): JSX.Element {
  return (
    <div className="guests">
      <Popover
        placement="bottomLeft"
        title={"Guests"}
        content={
          <Guests guestsList={guestsList} setGuestsList={setGuestsList} />
        }
        arrow={false}
        trigger="click"
      >
        <div className="details">
          {guestsList.adults + guestsList.children} Guests
        </div>
      </Popover>
    </div>
  );
}

function Search({
  dates,
  guestsList,
}: {
  dates: Date[];
  guestsList: bookings.IGuestList;
}): JSX.Element {
  const pathname = usePathname();
  const router = useRouter();
  const searchParam = useSearchParams();

  const onSearch = () => {
    const query = addValuesToQueryString(searchParam, [
      {
        key: "availableDates",
        value: JSON.stringify({
          startDate: {
            operator: rooms.QueryRelationalOperatorEnum.GREATER_THAN_OR_EQUAL,
            startDate: dates[0],
          },
          endDate: {
            operator: rooms.QueryRelationalOperatorEnum.LESS_THAN_OR_EQUAL,
            endDate: dates[1],
          },
        }),
      },

      {
        key: "capacity",
        value: JSON.stringify({
          operator: rooms.QueryRelationalOperatorEnum.GREATER_THAN_OR_EQUAL,
          capacity: guestsList.adults + guestsList.children,
        }),
      },

      {
        key: "adults",
        value: guestsList.adults.toString(),
      },

      {
        key: "children",
        value: guestsList.children.toString(),
      },
    ]);

    router.push(`${pathname === "/" ? "/search" : pathname}?${query}`);
  };

  return (
    <button className="search" onClick={onSearch}>
      Search
    </button>
  );
}

export default Searchbox;

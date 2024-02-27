"use client";
import React, { useEffect, useState } from "react";
import StatisticCard, {
  StatisticCardChartType,
  StatisticCardProps,
} from "../StatisticCard/StatisticCard";
import { organizations, payments } from "@/typings";
import paymentService from "@/services/myRoom/payment/paymentService";

export default function TotalRevenue({
  organization,
  error,
}: {
  organization: organizations.IOrganization;
  error: (message: string) => void;
}) {
  const [monthData, setMonthDate] =
    React.useState<payments.IRevenuStatistic | null>(null);
  const [weekData, setWeekDate] =
    React.useState<payments.IRevenuStatistic | null>(null);

  useEffect(() => {
    const currDate = new Date();

    const queryForMonthData: payments.IGetPaymentStatisticQuery = {
      organizationId: organization.id,
      startDate: new Date(
        currDate.getFullYear(),
        currDate.getMonth(),
        1
      ).toISOString(),
      endDate: currDate.toISOString(),
    };

    const queryForWeekData: payments.IGetPaymentStatisticQuery = {
      organizationId: organization.id,
      startDate: new Date(
        currDate.setDate(currDate.getDate() - 7)
      ).toISOString(),
      endDate: new Date().toISOString(),
    };

    paymentService
      .getPaymentStatistic(queryForMonthData)
      .then((data) => {
        setMonthDate(data.data);
      })
      .catch((err) => {
        console.log(error);
        error(err.message);
      });

    paymentService
      .getPaymentStatistic(queryForWeekData)
      .then((data) => {
        setWeekDate(data.data);
      })
      .catch((err) => {
        console.log(error);
        error(err.message);
      });
  }, [organization.id]);

  const [data, setDate] = useState<any>(null);

  useEffect(() => {
    if (monthData && weekData) {
      const myRevenus = weekData.data.map((value, index) => {
        return {
          name: "Revenue",
          Revenue: value,
        };
      });

      const finalDate: StatisticCardProps = {
        title: "Total Revenue",
        key: "Revenue",
        total: weekData.totalRevenue,
        thisMonth: monthData.total,
        thisWeek: weekData.total,
        data: myRevenus,
        chartType: StatisticCardChartType.BAR,
      };

      setDate(finalDate);
    }
  }, [monthData, weekData]);

  return data ? <StatisticCard data={data} /> : null;
}

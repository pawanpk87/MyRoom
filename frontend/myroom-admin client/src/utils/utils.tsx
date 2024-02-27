export function formatShortDate(date: Date): string {
  date = new Date(date);
  return date
    .toLocaleDateString("en-GB", {
      day: "numeric",
      month: "short",
      year: "numeric",
    })
    .replace(/ /g, "-");
}

export const getError = (error: any) => {
  return error.response && error.response.data && error.response.data.message
    ? error.response.data.message
    : error.message;
};

export function calculatePercentage(number: number, totalNumber: number) {
  return Number(((number / totalNumber) * 100).toFixed(0));
}

export function getWeekDayList(): string[] {
  var weekDays = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
  var today = new Date().getDay();
  var rotatedWeekDays = weekDays.slice(today).concat(weekDays.slice(0, today));
  return rotatedWeekDays;
}

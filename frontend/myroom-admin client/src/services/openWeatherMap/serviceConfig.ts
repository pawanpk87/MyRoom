import axios, { AxiosInstance } from "axios";
import queryString from "query-string";

const axiosClient: AxiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_OPENWEATHERMAP_URI,

  headers: {
    "Content-Type": "application/json",
  },

  paramsSerializer: (params) => {
    const query = queryString.stringify({
      ...params,
      appid: process.env.NEXT_PUBLIC_OPENWEATHERMAP_API_KEY,
    });
    return query;
  },
});

export default axiosClient;

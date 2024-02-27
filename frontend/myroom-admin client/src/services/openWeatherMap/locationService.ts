import axiosClient from "./serviceConfig";
import { geocoding } from "../../typings/geocoding";

const locationService = {
  getCities: (params: { q: string; limit: number }) => {
    const url = `/geo/1.0/direct`;
    return axiosClient.get(url, { params });
  },

  getLocation: (params: geocoding.coords) => {
    const url = `/geo/1.0/reverse`;
    return axiosClient.get(url, { params });
  },
};

export default locationService;

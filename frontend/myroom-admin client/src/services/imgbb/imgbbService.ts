import axiosClient from "./serviceConfig";
import { imgbb } from "@/typings";

const imgbbService = {
  uploadImage: (params: imgbb.IImgbbData) => {
    const url = "/1/upload";
    return axiosClient.post(url, params.data);
  },
};

export default imgbbService;

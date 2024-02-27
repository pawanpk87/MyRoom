import { auth } from "@/typings";
import axiosClient, { setAuthTokenForAuthService } from "./serviceConfig";

const authService = {
  getUser: (id: string) => {
    setAuthTokenForAuthService();
    const url = `/users/${id}`;
    return axiosClient.get(url);
  },
};

export const verfiyToken = async (accessToken: string): Promise<boolean> => {
  try {
    const verifyTokenResponse = await fetch(
      `${process.env.NEXT_PUBLIC_MYROOM_API_URI}${process.env.NEXT_PUBLIC_ROOM_AUTH_SERVER_URL}/verifyToken`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    const data: auth.IVerifyTokenResponse = await verifyTokenResponse.json();
    return data.status === auth.AuthStatus.SUCCESS;
  } catch (error: any) {
    return false;
  }
};

export default authService;

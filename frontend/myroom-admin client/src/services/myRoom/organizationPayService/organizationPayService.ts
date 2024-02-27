import axiosClient, {
  setAuthTokenForOrganizationPayService,
} from "./serviceConfig";

const organizationPayService = {
  getOrganizationAccountDetailsOrganizationId: (id: string) => {
    setAuthTokenForOrganizationPayService();
    const url = `/account/${id}`;
    return axiosClient.get(url);
  },

  getNewAccountLink: (accountId: string) => {
    setAuthTokenForOrganizationPayService();
    const url = `/onboarding?accountId=${accountId}`;
    return axiosClient.get(url);
  },
};

export default organizationPayService;

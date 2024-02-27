import axiosClient, {
  setAuthTokenForOrganizationService,
} from "./serviceConfig";

const organizationService = {
  getOrganizationBySuperAdminId: (id: string) => {
    setAuthTokenForOrganizationService();
    const url = `/org/user/${id}`;
    return axiosClient.get(url);
  },
};

export default organizationService;

import axiosClient, { setAuthTokenForOnboardingService } from "./serviceConfig";
import { onboarding } from "@/typings";

const onboardingService = {
  onboardOrganization: (data: onboarding.IOnboardOrganization) => {
    setAuthTokenForOnboardingService();
    const url = `/organization`;
    return axiosClient.post(url, data);
  },

  onboardOrganizationBankAccount: (
    data: onboarding.IOrganizationAccountOnboarding
  ) => {
    setAuthTokenForOnboardingService();
    const url = `/organization/${data.id}/account`;
    return axiosClient.post(url, data);
  },
};

export default onboardingService;

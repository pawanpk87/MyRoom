"use client";
import React, { useEffect, useState } from "react";
import { useUserAuth } from "@/firebase/auth/authProvider";
import "./organization.css";
import { organizations } from "@/typings";
import { Button, Descriptions, DescriptionsProps, message } from "antd";
import AdminCard from "@/components/Admin/AdminCard/AdminCard";
import organizationPayService from "@/services/myRoom/organizationPayService/organizationPayService";
import { organizationpay } from "@/typings/organizationpay";
import { getError } from "@/utils/utils";

export default function Organization() {
  const [messageApi, contextHolder] = message.useMessage();

  const { user, organization } = useUserAuth();

  const error = (message: string) => {
    messageApi.open({
      type: "error",
      content: message,
    });
  };

  return (
    organization && (
      <div className="organizationMain">
        <OrganizationProfile organization={organization} />
        <OrganizationAccount organization={organization} errorMessage={error} />
      </div>
    )
  );
}

const OrganizationProfile = ({
  organization,
}: {
  organization: organizations.IOrganization;
}): JSX.Element | null => {
  const [organizationDetailItems, setOrganizationDetailItems] = useState<
    any[] | null
  >(null);

  useEffect(() => {
    if (organization) {
      const items: DescriptionsProps["items"] = [
        {
          key: "2",
          label: "Email",
          children: organization.email,
        },
        {
          key: "3",
          label: "Description",
          children: organization.description,
        },
        {
          key: "4",
          label: "Phone",
          children: organization.phone,
        },
        {
          key: "5",
          label: "Address",
          children:
            "No. 18, Wantang Road, Xihu District, Hangzhou, Zhejiang, China",
        },
      ];
      setOrganizationDetailItems([...items]);
    }
  }, [organization]);

  return (
    organizationDetailItems && (
      <OrganizationDescriptionCard
        title={`${organization.name} Profile`}
        id={organization.id}
        description={
          <>
            <Descriptions title={null} items={organizationDetailItems} />
            <br />
            <AdminCard
              id={organization?.superAdmin?.uid!}
              type={organization?.superAdmin?.adminType!}
            />
          </>
        }
      />
    )
  );
};

const OrganizationAccount = ({
  organization,
  errorMessage,
}: {
  organization: organizations.IOrganization;
  errorMessage: (message: string) => void;
}): JSX.Element | null => {
  const [organizationAccountDetailItems, setOrganizationAccountDetailItems] =
    useState<any[] | null>(null);

  const [organizationAccountDetails, setOrganizationAccountDetails] =
    useState<organizationpay.IOrganizationAccount | null>(null);

  const [loading, setLoading] = useState<boolean>(false);

  useEffect(() => {
    organizationPayService
      .getOrganizationAccountDetailsOrganizationId(organization.id)
      .then((data) => {
        const receivedOrganizationAccountDetails: organizationpay.IOrganizationAccount =
          data.data;

        const items: DescriptionsProps["items"] = [
          {
            key: "1",
            label: "id",
            children: receivedOrganizationAccountDetails.id,
          },
          {
            key: "2",
            label: "organizationId",
            children: receivedOrganizationAccountDetails.id,
          },

          {
            key: "3",
            label: "status",
            children: receivedOrganizationAccountDetails.status,
          },

          {
            key: "3",
            label: "createdAt",
            children: receivedOrganizationAccountDetails.createdAt,
          },

          {
            key: "3",
            label: "updatedAt",
            children: receivedOrganizationAccountDetails.updatedAt,
          },
        ];

        setOrganizationAccountDetailItems([...items]);
        setOrganizationAccountDetails({
          ...receivedOrganizationAccountDetails,
        });
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  const onActivateAccount = (accountId: string) => {
    setLoading(false);
    organizationPayService
      .getNewAccountLink(accountId)
      .then((response) => {
        window.location.href = response.data;
        setLoading(false);
      })
      .catch((error) => {
        setLoading(false);
        errorMessage(getError(error));
      });
  };

  return (
    organizationAccountDetailItems &&
    organizationAccountDetails && (
      <OrganizationDescriptionCard
        title={"Organization Account Details"}
        description={
          <>
            <Descriptions title={null} items={organizationAccountDetailItems} />
            <br />
            <div className="stripeAccount">
              <div className="stripeAccountHeader">
                <h4>Stripe Account</h4>
              </div>

              <div className="stripeAccountDetails">
                <div
                  style={{ display: "flex", flexDirection: "row", gap: "20px" }}
                >
                  <span>Account Id:</span>
                  <span>
                    {organizationAccountDetails.stripeAccountDetails.accountId}
                  </span>
                </div>

                <div
                  style={{ display: "flex", flexDirection: "row", gap: "20px" }}
                >
                  <span>Account status:</span>
                  <span>
                    {organizationAccountDetails.stripeAccountDetails.status}
                  </span>
                  {organizationAccountDetails.stripeAccountDetails.status !==
                    organizationpay.StripeAccountStatus.ACTIVE && (
                    <Button
                      type="primary"
                      onClick={() => {
                        onActivateAccount(
                          organizationAccountDetails.stripeAccountDetails
                            .accountId
                        );
                      }}
                      loading={loading}
                    >
                      Activate
                    </Button>
                  )}
                </div>
              </div>
            </div>
          </>
        }
      />
    )
  );
};

const OrganizationDescriptionCard = ({
  title,
  id,
  description,
}: {
  title: string;
  id: string;
  description: JSX.Element;
}): JSX.Element => {
  return (
    <div className="organizationDescriptionCard">
      <div className="organizationDescriptionCardHeader">
        <h3>
          {title} #{id}
        </h3>
      </div>
      <div className="organizationDescriptionCardDescription">
        {description}
      </div>
    </div>
  );
};

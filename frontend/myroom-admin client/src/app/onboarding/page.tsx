"use client";
import { useUserAuth } from "@/firebase/auth/authProvider";
import { useEffect, useState } from "react";
import { Button, Form, Input, Select, Spin, Divider, message } from "antd";
import { Option } from "antd/es/mentions";
import locationService from "@/services/openWeatherMap/locationService";
import { geocoding, onboarding } from "@/typings";
import onboardingService from "@/services/myRoom/onboarding/onboardingService";
import { useRouter } from "next/navigation";
import "./onboarding.css";
import { getError } from "@/utils/utils";

export default function Onboarding() {
  const router = useRouter();

  const { user, organization, isOrganizationDataLoading } = useUserAuth();

  const [loading, setLoading] = useState<boolean>(false);

  const [messageApi, contextHolder] = message.useMessage();

  const [name, setName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [phoneNumber, setPhoneNumber] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [businessProfile, setBusinessProfile] =
    useState<onboarding.IBusinessProfile>({
      businessType: "individual",
      category: "housing",
      subcategory: "space_rental",
      addresses: {
        street1: "",
        street2: "",
        city: "",
        state: "",
        postalCode: "",
        country: "",
      },
    });

  useEffect(() => {
    setLoading(true);
    if (user && !isOrganizationDataLoading) {
      if (user) {
        setEmail(user.email!);
      }

      if (organization) {
        router.push("/dashboard");
      }
      setLoading(false);
    }
  }, [user, isOrganizationDataLoading, organization]);

  const formItemLayout = {
    labelCol: {
      xs: { span: 24 },
      sm: { span: 6 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 14 },
    },
  };

  const onboardOrganization = async () => {
    setLoading(true);

    const onboardOrganization: onboarding.IOnboardOrganization = {
      name: name,
      email: email,
      phone: phoneNumber,
      description: description,
      businessProfile: businessProfile,
      uid: user?.uid!,
    };

    onboardingService
      .onboardOrganization(onboardOrganization)
      .then((data) => {
        success("Organization Created successfully!");

        onboardingService
          .onboardOrganizationBankAccount({
            id: data.data.id,
            uid: user?.uid!,
          })
          .then((response) => {
            window.location.href = response.data.link;
            // setTimeout(() => {
            //   setLoading(false);
            //   success("Organization Account Created successfully!");
            //   location.reload();
            // }, 2000);
          })
          .catch((err) => {
            setLoading(false);
            error(getError(err));
          });
      })
      .catch((err: any) => {
        setLoading(false);
        error(getError(err));
      });
  };

  const success = (message: string) => {
    messageApi.open({
      type: "success",
      content: message,
    });
  };

  const error = (message: string) => {
    messageApi.open({
      type: "error",
      content: message,
    });
  };

  const warning = (message: string) => {
    messageApi.open({
      type: "warning",
      content: message,
    });
  };

  return (
    user && (
      <div className="onboardingDivMain">
        <div className="content">
          <div className="header">
            <h2>Organization Onboarding</h2>
          </div>
          <Form
            {...formItemLayout}
            style={{ maxWidth: 600 }}
            onFinish={onboardOrganization}
          >
            <Name name={name} setName={setName} />

            <Email email={user.email!} />

            <PhoneNumber
              phoneNumber={phoneNumber}
              setPhoneNumber={setPhoneNumber}
            />

            <Description
              description={description}
              setDescription={setDescription}
            />

            <BusinessProfile
              businessProfile={businessProfile}
              setBusinessProfile={setBusinessProfile}
            />

            <Form.Item wrapperCol={{ offset: 6, span: 16 }}>
              <Button type="primary" htmlType="submit" loading={loading}>
                Submit
              </Button>
            </Form.Item>
          </Form>
        </div>
        <Spin size="large" className="loadingIcon" spinning={loading} />
        {contextHolder}
      </div>
    )
  );
}

function Name({
  name,
  setName,
}: {
  name: string;
  setName: React.Dispatch<React.SetStateAction<string>>;
}): JSX.Element {
  return (
    <Form.Item
      label="Company Name"
      name="companyName"
      rules={[{ required: true, message: "Please input!" }]}
    >
      <Input value={name} onChange={(e) => setName(e.target.value)} />
    </Form.Item>
  );
}

function Email({ email }: { email: string }): JSX.Element {
  return (
    <Form.Item label="Email" name="email">
      <Input value={email} defaultValue={email} disabled={true} />
    </Form.Item>
  );
}

function PhoneNumber({
  phoneNumber,
  setPhoneNumber,
}: {
  phoneNumber: string;
  setPhoneNumber: React.Dispatch<React.SetStateAction<string>>;
}): JSX.Element {
  const [prefixNumber, setPrefixNumber] = useState<string>("");

  const prefixSelector = (
    <Form.Item name="prefix" noStyle>
      <Select
        style={{ width: 70 }}
        onChange={(value) => setPrefixNumber(value)}
      >
        <Option value="+91">+91</Option>
      </Select>
    </Form.Item>
  );

  return (
    <Form.Item
      name="Phone Number"
      label="phoneNumber"
      rules={[{ required: true, message: "phoneNumber is required" }]}
    >
      <Input
        addonBefore={prefixSelector}
        style={{ width: "100%" }}
        value={phoneNumber}
        onChange={(e) => setPhoneNumber(`${prefixNumber}${e.target.value}`)}
      />
    </Form.Item>
  );
}

function Description({
  description,
  setDescription,
}: {
  description: string;
  setDescription: React.Dispatch<React.SetStateAction<string>>;
}): JSX.Element {
  return (
    <Form.Item
      label="Description"
      name="description"
      rules={[{ required: true, message: "Please input!" }]}
    >
      <Input.TextArea
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
    </Form.Item>
  );
}

function BusinessProfile({
  businessProfile,
  setBusinessProfile,
}: {
  businessProfile: onboarding.IBusinessProfile;
  setBusinessProfile: React.Dispatch<
    React.SetStateAction<onboarding.IBusinessProfile>
  >;
}): JSX.Element {
  return (
    <>
      <Divider plain>Business Profile</Divider>

      <Form.Item
        label="Type"
        rules={[{ required: true, message: "Please input!" }]}
      >
        <Select
          onChange={(value) =>
            setBusinessProfile({ ...businessProfile, businessType: value })
          }
        >
          <Select.Option value="individual">individual</Select.Option>
        </Select>
      </Form.Item>

      <Form.Item
        label="Category"
        rules={[{ required: true, message: "Please input!" }]}
      >
        <Select
          onChange={(value) =>
            setBusinessProfile({ ...businessProfile, category: value })
          }
        >
          <Select.Option value="housing">housing</Select.Option>
        </Select>
      </Form.Item>

      <Form.Item
        label="Subcategory"
        rules={[{ required: true, message: "Please input!" }]}
      >
        <Select
          onChange={(value) =>
            setBusinessProfile({ ...businessProfile, subcategory: value })
          }
        >
          <Select.Option value="space_rental">space_rental</Select.Option>
        </Select>
      </Form.Item>

      <Address
        businessProfile={businessProfile}
        setBusinessProfile={setBusinessProfile}
      />

      <Divider />
    </>
  );
}

function Address({
  businessProfile,
  setBusinessProfile,
}: {
  businessProfile: onboarding.IBusinessProfile;
  setBusinessProfile: React.Dispatch<
    React.SetStateAction<onboarding.IBusinessProfile>
  >;
}) {
  const [addresses, setAddress] = useState<onboarding.IAddresses>(
    businessProfile.addresses
  );

  const [cities, setCities] = useState<any>([]);
  const [cityInput, setCityInput] = useState<string | null>(
    businessProfile.addresses.city
  );

  const handleSearch = (newValue: string) => {
    if (newValue.length > 2) {
      locationService
        .getCities({
          q: newValue,
          limit: 10,
        })
        .then((cities) => {
          let allCities = cities.data.map((city: geocoding.ICity) => {
            return {
              value: {
                city: city.name,
                state: city.state,
                country: city.country,
              },
              text: `${city.name}${city.state ? "," + city.state : ""},${
                city.country
              }`,
            };
          });
          setCities(allCities);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  const handleChange = (newValue: string) => {
    setCityInput(newValue);
  };

  const handleSelect = (
    selectedValue: string,
    data: {
      data: {
        city: string;
        state: string;
        country: string;
      };
    }
  ) => {
    setAddress({
      ...addresses,
      city: data.data.city,
      country: data.data.country,
      state: data.data.state,
    });

    setBusinessProfile({
      ...businessProfile,
      addresses: {
        ...addresses,
        city: data.data.city,
        country: data.data.country,
        state: data.data.state,
      },
    });
  };

  return (
    <>
      <Form.Item
        label="Street1"
        name="street1"
        rules={[{ required: true, message: "Please input!" }]}
      >
        <Input
          value={businessProfile.addresses.street1}
          onChange={(value) => {
            setAddress({ ...addresses, street1: value.target.value });
            setBusinessProfile({
              ...businessProfile,
              addresses: { ...addresses, street1: value.target.value },
            });
          }}
        />
      </Form.Item>

      <Form.Item label="Street2" name="street2">
        <Input
          value={businessProfile.addresses.street2}
          onChange={(value) => {
            setAddress({ ...addresses, street2: value.target.value });
            setBusinessProfile({
              ...businessProfile,
              addresses: { ...addresses, street2: value.target.value },
            });
          }}
        />
      </Form.Item>

      <Form.Item
        label="City"
        rules={[{ required: true, message: "Please input!" }]}
      >
        <Select
          showSearch
          autoClearSearchValue
          value={cityInput}
          placeholder={"search"}
          defaultActiveFirstOption={false}
          suffixIcon={null}
          filterOption={false}
          onSearch={handleSearch}
          onChange={handleChange}
          onSelect={handleSelect}
          notFoundContent={null}
          options={(cities || []).map((d: any) => ({
            value: d.value.city,
            label: d.text,
            data: d.value,
          }))}
        />
      </Form.Item>

      <Form.Item
        label="Postal Code"
        name="postalCode"
        rules={[{ required: true, message: "Please input!" }]}
      >
        <Input
          value={businessProfile.addresses.postalCode}
          onChange={(value) => {
            setAddress({ ...addresses, postalCode: value.target.value });
            setBusinessProfile({
              ...businessProfile,
              addresses: { ...addresses, postalCode: value.target.value },
            });
          }}
        />
      </Form.Item>
    </>
  );
}

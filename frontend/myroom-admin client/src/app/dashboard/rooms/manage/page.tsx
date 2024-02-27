"use client";
import React, { useEffect, useState } from "react";
import {
  notFound,
  usePathname,
  useRouter,
  useSearchParams,
} from "next/navigation";
import {
  CloseCircleOutlined,
  InfoCircleOutlined,
  PlusOutlined,
} from "@ant-design/icons";
import {
  Button,
  DatePicker,
  Divider,
  Form,
  Input,
  Select,
  Slider,
  Upload,
  Tag,
  notification,
  message,
} from "antd";
import { geocoding, rooms } from "@/typings";
const { RangePicker } = DatePicker;
const { TextArea } = Input;
import { SelectProps } from "antd/lib";
import locationService from "@/services/openWeatherMap/locationService";
import roomService from "@/services/myRoom/room/roomService";
import imgbbService from "@/services/imgbb/imgbbService";
import "./manage.css";
import { useUserAuth } from "@/firebase/auth/authProvider";

export default function ManageRoom() {
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();

  const { user, organization } = useUserAuth();

  const [loading, setLoading] = React.useState<boolean>(false);
  const [toastApi, toastContextHolder] = notification.useNotification();

  const [title, setTitle] = React.useState<string>("");
  const [description, setDescription] = React.useState<string>("");
  const [availableDates, setAvailableDates] =
    React.useState<rooms.AvailableDates>({
      startDate: new Date(),
      endDate: new Date(),
    });
  const [capacity, setCapacity] = React.useState<number>(2);
  const [note, setNote] = React.useState<string>("");
  const [bookingStatus, setBookingStatus] = React.useState<rooms.BookingStatus>(
    rooms.BookingStatus.AVAILABLE
  );
  const [checkInTypes, setCheckInTypes] = React.useState<rooms.CheckInType[]>([
    rooms.CheckInType.ONLINE_PAYMENT,
  ]);
  const [prices, setPrices] = React.useState<rooms.Prices>({
    price: 0,
    cleaningFee: 0,
    roomService: 0,
    currency: rooms.Currency.INR.toString(),
  });
  const [amenities, setAmenities] = React.useState<string[]>([]);
  const [features, setFeatures] = React.useState<string[]>([]);
  const [city, setCity] = React.useState<string>("");
  const [address, setAddress] = React.useState<string>("");
  const [mainImage, setMainImage] = React.useState<string>("");

  let id = searchParams.get("id");
  let type = searchParams.get("type");

  if (type == "Edit Room" || type == "Delete Room") {
    if (!id) {
      notFound();
    }
  }

  useEffect(() => {
    if (type !== "Add Room") {
      const fetchRoom = async () => {
        roomService
          .getRoom(id!)
          .then((data) => {
            const room: rooms.IRoomData = data.data;
            setTitle(room.title);
            setDescription(room.description);
            setAvailableDates(room.availableDates);
            setCapacity(room.capacity);
            setNote(room.note!);
            setBookingStatus(room.bookingStatus);
            setCheckInTypes(room.checkInTypes);
            setPrices(room.prices);
            setAmenities(room.amenities);
            setFeatures(room.features);
            setCity(room.city);
            setAddress(room.address);
            setMainImage(room.mainImage);
          })
          .catch((error) => {
            if (error.response && error.response.status === 404) {
              notFound();
            } else {
              toastApi.open({
                message: "Error",
                description: error.response.data.message,
                icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
              });
            }
          });
      };
      fetchRoom();
    }
  }, [id]);

  const onAdd = () => {
    setLoading(true);
    if (
      title &&
      description &&
      availableDates &&
      capacity &&
      // note &&
      bookingStatus &&
      checkInTypes &&
      prices &&
      amenities &&
      features &&
      city &&
      address &&
      mainImage
    ) {
      const createRoomData: rooms.ICreateRoomData = {
        uid: user?.uid!,
        organizationId: organization?.id!,
        title,
        description,
        availableDates,
        prices,
        note,
        capacity,
        bookingStatus,
        amenities,
        mainImage,
        features,
        city,
        address,
        checkInTypes,
      };
      roomService
        .createRoom(createRoomData)
        .then((response) => {
          setLoading(false);
          toastApi.open({
            message: "Success",
            description: "Room Created",
            icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
          });
          setTimeout(() => {
            setLoading(false);
            router.push("/dashboard/rooms");
          }, 2000);
        })
        .catch((error: any) => {
          setLoading(false);
          toastApi.open({
            message: "Error",
            description: error.response.data.message,
            icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
          });
        });
    } else {
      setLoading(false);
      toastApi.open({
        message: "Error",
        description: "Please fill all details",
        icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
      });
    }
  };

  const onEdit = () => {
    setLoading(true);
    if (
      title &&
      description &&
      availableDates &&
      capacity &&
      bookingStatus &&
      checkInTypes &&
      prices &&
      amenities &&
      features &&
      city &&
      address &&
      mainImage
    ) {
      const updateRoomData: rooms.IUpdateRoomData = {
        id: id!,
        uid: user?.uid!,
        title,
        description,
        availableDates,
        prices,
        note,
        capacity,
        bookingStatus,
        amenities,
        mainImage,
        features,
        city,
        address,
        checkInTypes,
      };
      roomService
        .updateRoom(updateRoomData)
        .then((response) => {
          setLoading(false);
          toastApi.open({
            message: "Success",
            description: "Updated room successfully",
            icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
          });

          setTimeout(() => {
            setLoading(false);
            router.push("/dashboard/rooms");
          }, 2000);
        })
        .catch((error: any) => {
          setLoading(false);
          toastApi.open({
            message: "Error",
            description: error.response.data.message,
            icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
          });
        });
    } else {
      setLoading(false);
      toastApi.open({
        message: "Error",
        description: "Please fill all details",
        icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
      });
    }
  };

  const onDelete = () => {
    setLoading(true);
    const deleteRoomData: rooms.IDeleteRoom = {
      id: id!,
      uid: user?.uid!,
    };

    roomService
      .deleteRoom(deleteRoomData)
      .then((response) => {
        setLoading(false);
        toastApi.open({
          message: "Success",
          description: "Delete room successfully!",
          icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
        });
      })
      .catch((error: any) => {
        setLoading(false);
        toastApi.open({
          message: "Error",
          description: error.response.data.message,
          icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
        });
      });
  };

  const onClick = () => {
    if (type === "Add Room") {
      onAdd();
    } else if (type === "Edit Room") {
      onEdit();
    } else if (type === "Delete Room") {
      onDelete();
    }
  };

  return (
    <div className="manage">
      <div>
        <h2>Add Room</h2>
      </div>
      <br />
      <br />
      <div>
        <Form
          labelCol={{ span: 4 }}
          wrapperCol={{ span: 14 }}
          layout="horizontal"
          style={{ maxWidth: 600 }}
          disabled={type == "Delete Room"}
        >
          <Title title={title} setTitle={setTitle} />

          {type === "Add Room" && <MainImage setMainImage={setMainImage} />}

          <Description
            description={description}
            setDescription={setDescription}
          />

          <AvailableDates
            availableDates={availableDates}
            setAvailableDates={setAvailableDates}
          />

          <Capacity capacity={capacity} setCapacity={setCapacity} />

          <Note note={note} setNote={setNote} />

          <BookingStatus
            bookingStatus={bookingStatus}
            setBookingStatus={setBookingStatus}
          />

          <CheckInType
            checkInTypes={checkInTypes}
            setCheckInTypes={setCheckInTypes}
          />

          <Divider />
          <Prices prices={prices} setPrices={setPrices} />

          <Divider />
          <Amenties amenities={amenities} setAmenities={setAmenities} />

          <Divider />
          <Features features={features} setFeatures={setFeatures} />

          <Divider />

          <Address
            city={city}
            address={address}
            setCity={setCity}
            setAddress={setAddress}
          />
        </Form>
        <Button type="primary" loading={loading} onClick={onClick}>
          {type}
        </Button>
      </div>
      {toastContextHolder}
    </div>
  );
}

function Title({
  title,
  setTitle,
}: {
  title: string;
  setTitle: React.Dispatch<React.SetStateAction<string>>;
}) {
  return (
    <Form.Item label="Title" rules={[{ required: true }]}>
      <Input value={title} onChange={(e) => setTitle(e.target.value)} />
    </Form.Item>
  );
}

function Description({
  description,
  setDescription,
}: {
  description: string;
  setDescription: React.Dispatch<React.SetStateAction<string>>;
}) {
  return (
    <Form.Item label="Description" rules={[{ required: true }]}>
      <TextArea
        rows={4}
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
    </Form.Item>
  );
}

function AvailableDates({
  availableDates,
  setAvailableDates,
}: {
  availableDates: rooms.AvailableDates;
  setAvailableDates: React.Dispatch<React.SetStateAction<rooms.AvailableDates>>;
}) {
  return (
    <Form.Item label="AvailableDates" rules={[{ required: true }]}>
      <RangePicker
        onChange={(values) => {
          if (values && values.length > 0) {
            setAvailableDates({
              startDate: values[0].toDate(),
              endDate: values[1].toDate(),
            });
          }
        }}
      />
    </Form.Item>
  );
}

function Capacity({
  capacity,
  setCapacity,
}: {
  capacity: number;
  setCapacity: React.Dispatch<React.SetStateAction<number>>;
}) {
  return (
    <Form.Item label="Capacity">
      <Slider
        min={1}
        max={100}
        value={capacity}
        onChange={(value) => setCapacity(value)}
      />
    </Form.Item>
  );
}

function Note({
  note,
  setNote,
}: {
  note: string;
  setNote: React.Dispatch<React.SetStateAction<string>>;
}) {
  return (
    <Form.Item label="Note" rules={[{ required: true }]}>
      <TextArea
        rows={4}
        value={note}
        onChange={(e) => setNote(e.target.value)}
      />
    </Form.Item>
  );
}

function BookingStatus({
  bookingStatus,
  setBookingStatus,
}: {
  bookingStatus: rooms.BookingStatus;
  setBookingStatus: React.Dispatch<React.SetStateAction<rooms.BookingStatus>>;
}) {
  return (
    <Form.Item label="Booking Status">
      <Select
        value={bookingStatus}
        onSelect={(value) => setBookingStatus(value)}
      >
        <Select.Option value={rooms.BookingStatus.AVAILABLE}>
          AVAILABLE
        </Select.Option>
        <Select.Option value={rooms.BookingStatus.BOOKED}>BOOKED</Select.Option>
        <Select.Option value={rooms.BookingStatus.MAINTENANCE}>
          MAINTENANCE
        </Select.Option>
        <Select.Option value={rooms.BookingStatus.OCCUPIED}>
          OCCUPIED
        </Select.Option>
      </Select>
    </Form.Item>
  );
}

function CheckInType({
  checkInTypes,
  setCheckInTypes,
}: {
  checkInTypes: rooms.CheckInType[];
  setCheckInTypes: React.Dispatch<React.SetStateAction<rooms.CheckInType[]>>;
}) {
  return (
    <Form.Item label="Check In Type">
      <Select
        value={checkInTypes}
        onSelect={(value) => setCheckInTypes([...checkInTypes, value])}
      >
        <Select.Option value={rooms.CheckInType.ONLINE_PAYMENT}>
          ONLINE_PAYMENT
        </Select.Option>
        <Select.Option value={rooms.CheckInType.PAY_AT_HOTEL}>
          PAY_AT_HOTEL
        </Select.Option>
      </Select>
    </Form.Item>
  );
}

function Prices({
  prices,
  setPrices,
}: {
  prices: rooms.Prices;
  setPrices: React.Dispatch<React.SetStateAction<rooms.Prices>>;
}) {
  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        gap: "15px",
      }}
    >
      <h3>Prices</h3>

      <div
        style={{
          flex: 1,
        }}
      >
        <Form.Item label="Price" rules={[{ required: true }]}>
          <Input
            type="number"
            value={prices.price}
            onChange={(e) => {
              setPrices({
                ...prices,
                price: Number(e.target.value),
              });
            }}
          />
        </Form.Item>
        <Form.Item label="Cleaning Fee" rules={[{ required: true }]}>
          <Input
            type="number"
            value={prices.cleaningFee}
            onChange={(e) => {
              setPrices({
                ...prices,
                cleaningFee: Number(e.target.value),
              });
            }}
          />
        </Form.Item>
        <Form.Item label="Room Service" rules={[{ required: true }]}>
          <Input
            type="number"
            value={prices.roomService}
            onChange={(e) => {
              setPrices({
                ...prices,
                roomService: Number(e.target.value),
              });
            }}
          />
        </Form.Item>
        <Form.Item label="Currency">
          <Select
            value={prices.currency}
            defaultValue={"inr"}
            onSelect={(value) => {
              setPrices({
                ...prices,
                currency: value,
              });
            }}
          >
            <Select.Option value="inr">INR</Select.Option>
          </Select>
        </Form.Item>
      </div>
    </div>
  );
}

function Amenties({
  amenities,
  setAmenities,
}: {
  amenities: string[];
  setAmenities: React.Dispatch<React.SetStateAction<string[]>>;
}) {
  const [value, setValue] = React.useState<string>("");

  const onAdd = () => {
    setAmenities([...amenities, value]);
    setValue("");
  };

  const onRemove = (targetValue: string) => {
    const filteredAmenities = amenities.filter(
      (value) => value !== targetValue
    );
    setAmenities([...filteredAmenities]);
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        gap: "15px",
      }}
    >
      <h3>Amenties</h3>

      <div
        style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <div style={{ flex: 1 }}>
          <Form.Item label="Amenitie" rules={[{ required: true }]}>
            <Input value={value} onChange={(e) => setValue(e.target.value)} />
          </Form.Item>
        </div>

        <div>
          <Button onClick={onAdd}>Add</Button>
        </div>
      </div>

      <div>
        {amenities.map((amenitie, index) => (
          <Tag
            key={index}
            closeIcon={<CloseCircleOutlined />}
            style={{
              margin: "2px",
            }}
            onClick={() => {
              onRemove(amenitie);
            }}
          >
            {amenitie}
          </Tag>
        ))}
      </div>
    </div>
  );
}

function Features({
  features,
  setFeatures,
}: {
  features: string[];
  setFeatures: React.Dispatch<React.SetStateAction<string[]>>;
}) {
  const [value, setValue] = React.useState<string>("");

  const onAdd = () => {
    setFeatures([...features, value]);
    setValue("");
  };

  const onRemove = (targetValue: string) => {
    const filteredAmenities = features.filter((value) => value !== targetValue);
    setFeatures([...filteredAmenities]);
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        gap: "15px",
      }}
    >
      <h3>Features</h3>

      <div
        style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <div style={{ flex: 1 }}>
          <Form.Item label="Feature" rules={[{ required: true }]}>
            <Input value={value} onChange={(e) => setValue(e.target.value)} />
          </Form.Item>
        </div>

        <div>
          <Button onClick={onAdd}>Add</Button>
        </div>
      </div>

      <div>
        {features.map((feature, index) => (
          <Tag
            key={index}
            closeIcon={<CloseCircleOutlined />}
            style={{
              margin: "2px",
            }}
            onClick={() => {
              onRemove(feature);
            }}
          >
            {feature}
          </Tag>
        ))}
      </div>
    </div>
  );
}

function Address({
  city,
  address,
  setCity,
  setAddress,
}: {
  city: string;
  address: string;
  setCity: React.Dispatch<React.SetStateAction<string>>;
  setAddress: React.Dispatch<React.SetStateAction<string>>;
}) {
  const [cities, setCities] = useState<SelectProps["options"]>([]);
  const [cityInput, setCityInput] = useState<string | null>(city);

  const handleSearch = (newValue: string) => {
    if (newValue.length > 2) {
      locationService
        .getCities({
          q: newValue,
          limit: 30,
        })
        .then((cities) => {
          let allCities = cities.data.map((city: geocoding.ICity) => {
            return {
              value: city.name,
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

  const handleSelect = (selectedValue: string) => {
    setCity(selectedValue);
  };

  return (
    <>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          gap: "15px",
        }}
      >
        <h3>Address</h3>

        <Form.Item label="City" rules={[{ required: true }]}>
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
            options={(cities || []).map((d) => ({
              value: d.value,
              label: d.text,
            }))}
          />
        </Form.Item>

        <Form.Item label="Address" rules={[{ required: true }]}>
          <TextArea
            rows={4}
            value={address}
            onChange={(e) => setAddress(e.target.value)}
          />
        </Form.Item>
      </div>
    </>
  );
}

function MainImage({
  setMainImage,
}: {
  setMainImage: React.Dispatch<React.SetStateAction<string>>;
}) {
  const normFile = (e: any) => {
    if (Array.isArray(e)) {
      return e;
    }
    return e?.fileList;
  };

  const customRequest = async (options: any) => {
    const { onSuccess, onError, file } = options;

    // const isJpgOrPng = file.type === "image/jpeg" || file.type === "image/png";
    // if (!isJpgOrPng) {
    //   message.error("You can only upload JPG/PNG file!");
    // }
    // const isLt2M = file.size / 1024 / 1024 < 2;
    // if (!isLt2M) {
    //   message.error("Image must smaller than 2MB!");
    // }

    const formData = new FormData();
    formData.append("key", process.env.NEXT_PUBLIC_IMGBB_API_KEY);
    formData.append("image", file);

    try {
      const response = await imgbbService.uploadImage({
        data: formData,
      });
      const data = response.data;
      setMainImage(data.data.url);
      onSuccess(data, file);
    } catch (error) {
      onError(error, null, file);
    }
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        gap: "15px",
      }}
    >
      <h3>Main Image</h3>
      <div
        style={{
          flex: 1,
        }}
      >
        <Form.Item
          label="Upload"
          valuePropName="fileList"
          getValueFromEvent={normFile}
        >
          <Upload
            customRequest={customRequest}
            listType="picture-card"
            maxCount={1}
          >
            <button style={{ border: 0, background: "none" }} type="button">
              <PlusOutlined />
              <div style={{ marginTop: 8 }}>Upload</div>
            </button>
          </Upload>
        </Form.Item>
      </div>
    </div>
  );
}

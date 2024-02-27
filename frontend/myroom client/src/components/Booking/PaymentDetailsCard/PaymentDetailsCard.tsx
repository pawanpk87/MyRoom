import paymentService from "@/services/myRoom/payment/paymentService";
import { bookings } from "@/typings";
import { formatShortDate } from "@/utils/utils";
import { Table, TableContainer, Tbody, Td, Tr } from "@chakra-ui/react";
import React, { useEffect } from "react";
import "./PaymentDetailsCard.css";

export default function PaymentDetailsCard({
  bookingId,
}: {
  bookingId: string;
}) {
  const [paymentDetails, setPaymentDetails] =
    React.useState<bookings.IPaymentDetails | null>(null);

  useEffect(() => {
    paymentService
      .getPaymentDataByBookingId(bookingId)
      .then((data) => {
        setPaymentDetails(data.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [bookingId]);

  return (
    paymentDetails && (
      <div className="paymentDetailsCard">
        <div>
          <h2>Payment Details</h2>
        </div>
        <div>
          <TableContainer>
            <Table size="sm">
              <Tbody>
                <Tr>
                  <Td>Payment Id</Td>
                  <Td isNumeric>{paymentDetails.id}</Td>
                </Tr>

                <Tr>
                  <Td>Payment Date</Td>
                  <Td isNumeric>
                    {formatShortDate(new Date(paymentDetails.createdAt))}
                  </Td>
                </Tr>
                <Tr>
                  <Td>amount</Td>
                  <Td isNumeric>{paymentDetails.amount}</Td>
                </Tr>
                <Tr>
                  <Td>currency</Td>
                  <Td isNumeric>{paymentDetails.currency}</Td>
                </Tr>
                <Tr>
                  <Td>Payment Service Provider</Td>
                  <Td isNumeric>{paymentDetails.type}</Td>
                </Tr>
              </Tbody>
            </Table>
          </TableContainer>
        </div>
      </div>
    )
  );
}

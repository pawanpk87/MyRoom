import { notFound } from "next/navigation";
import { reviews } from "@/typings/reviews";
import reviewService from "@/services/myRoom/review/reviewService";
import ReviewDetailCard from "@/components/Reviews/ReviewDetailCard";
import "./review.css";

async function getReview(id: string): Promise<reviews.IReviewData> {
  try {
    const review: reviews.IReviewData = (await reviewService.getReview(id))
      .data;
    return review;
  } catch (error: any) {
    notFound();
  }
}

export default async function Room({
  params,
}: {
  params: {
    id: string;
  };
}) {
  const id = params.id;

  if (!id) {
    notFound();
  }

  const review: reviews.IReviewData = await getReview(id);

  return (
    <div className="reviewdata">
      <ReviewDetailCard review={review} />
    </div>
  );
}

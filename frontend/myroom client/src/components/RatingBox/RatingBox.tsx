import { CSSProperties } from "react";
import "./RatingBox.css";

function RatingBox({
  rating,
  style,
}: {
  rating: number;
  style?: CSSProperties;
}) {
  return (
    <div className="ratingBox" style={style}>
      {rating} ★
    </div>
  );
}

export default RatingBox;

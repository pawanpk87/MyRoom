import "./reviews.css";

export default function RoomsLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="reviews">
      <div className="header">
        <div>
          <h3>Review</h3>
        </div>
      </div>
      {children}
    </div>
  );
}

import NavPagination from "@/components/Search/SearchNavPagination/SearchNavPagination";
import SearchRoomItem from "@/components/Search/SearchRoomItem/SearchRoomItem";
import roomService from "@/services/myRoom/rooms/roomService";
import { rooms } from "@/typings";

interface IQueryData {
  rooms: rooms.IRoomData[];
  totalRecods: number;
  totalPages: number;
  currentPage: number;
}

export default async function Search({
  searchParams,
}: {
  searchParams: {
    [key: string]: string | string[] | undefined;
  };
}) {
  const rooms: IQueryData = (await roomService.getRooms(searchParams)).data;

  return (
    <>
      <div className="items">
        {rooms.rooms.map((room) => (
          <SearchRoomItem key={room.id} room={room} />
        ))}
      </div>
      {rooms.totalPages > 0 && (
        <NavPagination
          totalRecods={rooms.totalRecods}
          totalPages={rooms.totalPages}
          currentPage={rooms.currentPage}
        />
      )}
    </>
  );
}

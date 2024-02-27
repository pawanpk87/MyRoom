export namespace geocoding {
  export interface ICity {
    name: string;
    lat: string;
    lon: string;
    country: string;
    state: string;
  }

  export type coords = {
    lat: number;
    lon: number;
  };

  interface IGeolocationCoordinates {
    coords: coords;
  }
}

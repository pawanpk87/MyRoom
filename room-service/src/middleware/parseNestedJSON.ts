import { Request, Response, NextFunction } from "express";
import { decodeQueryParams } from "../utils";

export default function parseNestedJSON(
  req: Request,
  res: Response,
  next: NextFunction
) {
  const decodedQuery = decodeQueryParams(req.query);

  for (const key in req.query) {
    if (req.query.hasOwnProperty(key)) {
      const value = req.query[key];
      if (typeof value === "string") {
        try {
          const parsedValue = JSON.parse(value);
          req.query[key] = parsedValue;
        } catch (error) {}
      }
    }
  }
  next();
}

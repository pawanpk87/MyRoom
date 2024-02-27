import { Request, Response, NextFunction } from "express";
import { AnyZodObject } from "zod";
import logger from "../config/logger";

const validate =
  (schema: AnyZodObject) =>
  async (req: Request, res: Response, next: NextFunction) => {
    try {
      await schema.parseAsync({
        body: req.body,
        query: req.query,
        params: req.params,
      });
      return next();
    } catch (error: any) {
      logger.error(`zod error: `, error);
      res.status(400).send(error.errors);
    }
  };

export default validate;

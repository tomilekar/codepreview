import {lectionSchema} from '../models/lection';

const Joi = require('@hapi/joi');

export const lectionValidationSchema = Joi.object({
    title: Joi.string().trim().required(),
    lections: Joi.array('attachments').items(lectionSchema)
  }
);
export default {
  section: async (req: any, res: any, next: any) => {

    const {error} = await lectionValidationSchema.validate(req.body);
    if (error) {
      return res.status(400).send(error.details[0].message);
    }
    next();
  },
};

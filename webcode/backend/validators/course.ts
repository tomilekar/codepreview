import {sectionSchema} from '../models/section';

const Joi = require('@hapi/joi');

export default {
  course: async (req: any, res: any, next: any) => {
    const schema = Joi.object({
      title: Joi.string().trim().required(),
      image: Joi.string().trim().required(),
      description: Joi.string().trim().required(),
      category: Joi.string().trim().required(),
      price: Joi.number().required(),
      rating: Joi.number().required(),
      sections: Joi.array('sections').item(sectionSchema)
    });
    const {error} = await schema.validate(req.body);
    if (error) {
      return res.status(400).send(error.details[0].message);
    }
    next();
  },
};

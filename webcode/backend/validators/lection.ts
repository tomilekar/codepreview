const Joi = require('@hapi/joi');

export default {
  lection: async (req: any, res: any, next: any) => {
    const schema = Joi.object({
      title: Joi.string().trim().required(),
      video: Joi.object({
        type: Joi.string().trim().required(),
        path: Joi.string().trim().required(),
      }),
      attachments: Joi.array().items(
        Joi.object({
          text: Joi.string().trim().required(),
          type: Joi.string().trim().required(),
          path: Joi.string().trim().required(),
        })
      ),
    });
    const { error } = await schema.validate(req.body);
    if (error) return res.status(400).send(error.details[0].message);
    next();
  },
};

const Joi = require('@hapi/joi');

export default {
  registration: async (req: any, res: any, next: any) => {
    const schema = Joi.object({
      username: Joi.string().alphanum().min(3).max(30).required().trim(),
      email: Joi.string()
        .email({ minDomainSegments: 2, tlds: { allow: ['com', 'net', 'de'] } })
        .trim()
        .required(),
      password: Joi.string()
        .pattern(new RegExp('^[a-zA-Z0-9]{6,30}$'))
        .required(),
      // tokens: Joi.array().items({ token: Joi.token() }),
    });
    const { error } = await schema.validate(req.body);
    if (error) return res.status(400).send(error.details[0].message);
    next();
  },
  user: async (req: any, res: any, next: any) => {
    const schema = Joi.object({
      email: Joi.string()
        .email({ minDomainSegments: 2, tlds: { allow: ['com', 'net', 'de'] } })
        .trim()
        .required(),
      password: Joi.string().min(6).required(),
    });
    const { error } = await schema.validate(req.body);
    if (error) return res.status(400).send(error.details[0].message);
    next();
  },
};

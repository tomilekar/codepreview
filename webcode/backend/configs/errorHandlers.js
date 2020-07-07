module.exports = (app) => {
  app.use((req, res, next) => {
    const err = new Error(`Endpoint not found - ${req.originalUrl}`);
    err.status = 404;
    next(err);
  });

  app.use((err, req, res, next) => {
    res.status(err.status || 500);
    res.json({
      error: {
        status: err.status || 500,
        message: err.message,
      },
    });
  });
};

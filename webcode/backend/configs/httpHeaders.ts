module.exports = async (app:any) => {
  app.use((req:any, res:any, next:any) => {
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader("Access-Control-Allow-Credentials", "true");
    res.setHeader(
      "Access-Control-Allow-Methods",
      "GET,HEAD,OPTIONS,POST,PUT, DELETE, PATCH, UPDATE"
    );
    res.setHeader(
      "Access-Control-Allow-Headers",
      "Access-Control-Allow-Headers, Origin,Accept, Authorization" +
      " X-Requested-With, Content-Type," +
      " Access-Control-Request-Method, Access-Control-Request-Headers, Authorization"
    );
    next();
  })};

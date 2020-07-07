const express = require("express");
const morgan = require("morgan");
const cors = require("cors");

module.exports = async (app) => {

  app.use(morgan("common"));
  app.use(express.json());
  app.use(express.urlencoded({ extended: true }));
};

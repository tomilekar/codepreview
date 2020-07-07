import express = require('express');
import connect from './configs/databases';
import config from './configs/app';
import routes from './routes/routes';

const app = express();

connect(config.mongoUrl);
require('./configs/httpHeaders')(app);
require('./configs/express')(app);

routes(app);

require('./configs/errorHandlers')(app);

app.listen(config.port, () => console.log(`Server up on :${config.port}`));

import mongoose = require('mongoose');

export interface IResource extends mongoose.Document {
  _id: string;
  type: string;
  path: string;
  text: string;
}

export const resourceSchema = new mongoose.Schema({

  text: {
    type: String,
    trim: true,
    required: false,
  },
  type: {
    type: String,
    trim: true,
    required: true,
  },
  path: {
    type: String,
    trim: true,
    required: true,
  },
});

export const resourceModel = mongoose.model<IResource>(
  'Resource',
  resourceSchema
);

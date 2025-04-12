

// === Backend (Node.js + Express) ===
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const app = express();
app.use(express.json());
app.use(cors());

// MongoDB Connection
mongoose.connect('mongodb://localhost:27017/jobportal', {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

// User Schema
const UserSchema = new mongoose.Schema({
  name: String,
  email: String,
  password: String,
  role: { type: String, enum: ['seeker', 'employer'], default: 'seeker' },
});
const User = mongoose.model('User', UserSchema);

// Job Schema
const JobSchema = new mongoose.Schema({
  title: String,
  description: String,
  company: String,
  location: String,
  employerId: mongoose.Schema.Types.ObjectId,
});
const Job = mongoose.model('Job', JobSchema);

// Authentication Routes
app.post('/auth/signup', async (req, res) => {
  const { name, email, password, role } = req.body;
  const hashedPassword = await bcrypt.hash(password, 10);
  const user = new User({ name, email, password: hashedPassword, role });
  await user.save();
  res.json({ message: 'User registered' });
});

app.post('/auth/login', async (req, res) => {
  const { email, password } = req.body;
  const user = await User.findOne({ email });
  if (!user || !(await bcrypt.compare(password, user.password))) {
    return res.status(400).json({ message: 'Invalid credentials' });
  }
  const token = jwt.sign({ userId: user._id, role: user.role }, 'secretkey');
  res.json({ token });
});

// Job Routes
app.post('/jobs', async (req, res) => {
  const { title, description, company, location, employerId } = req.body;
  const job = new Job({ title, description, company, location, employerId });
  await job.save();
  res.json({ message: 'Job posted successfully' });
});

app.get('/jobs', async (req, res) => {
  const jobs = await Job.find();
  res.json(jobs);
});

app.listen(5000, () => console.log('Server running on port 5000'));

// === Frontend (React.js) ===
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const App = () => {
  const [jobs, setJobs] = useState([]);
  const [form, setForm] = useState({ title: '', description: '', company: '', location: '' });

  useEffect(() => {
    axios.get('http://localhost:5000/jobs').then(res => setJobs(res.data));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    await axios.post('http://localhost:5000/jobs', form);
    setForm({ title: '', description: '', company: '', location: '' });
  };

  return (
    <div>
      <h1>Job Application Platform</h1>
      <form onSubmit={handleSubmit}>
        <input type='text' placeholder='Title' onChange={(e) => setForm({...form, title: e.target.value})} />
        <input type='text' placeholder='Description' onChange={(e) => setForm({...form, description: e.target.value})} />
        <input type='text' placeholder='Company' onChange={(e) => setForm({...form, company: e.target.value})} />
        <input type='text' placeholder='Location' onChange={(e) => setForm({...form, location: e.target.value})} />
        <button type='submit'>Post Job</button>
      </form>
      <h2>Job Listings</h2>
      <ul>
        {jobs.map((job) => (
          <li key={job._id}>{job.title} - {job.company} ({job.location})</li>
        ))}
      </ul>
    </div>
  );
};

export default App;

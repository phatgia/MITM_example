const express = require('express');
const cors = require('cors');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

// Endpoint kiểm tra
app.get('/', (req, res) => {
    res.send('Secure Chat Backend is running!');
});

// Endpoint Đăng nhập
app.post('/login', (req, res) => {
    const { username, password } = req.body;
    
    // Hardcode dữ liệu để demo nhanh
    if (username === 'admin' && password === '123456') {
        console.log(`[+] User đăng nhập thành công: ${username}`);
        return res.status(200).json({ success: true, token: 'demo-jwt-token-123' });
    }
    
    return res.status(401).json({ success: false, message: 'Invalid credentials' });
});

// Endpoint Gửi tin nhắn nhạy cảm
app.post('/messages', (req, res) => {
    const { token, content } = req.body;

    if (!token) {
        return res.status(401).json({ success: false, message: 'Unauthorized' });
    }

    console.log(`\n================================`);
    console.log(`[+] TÍN HIỆU MỚI TỪ CLIENT:`);
    console.log(`[+] Nội dung bản rõ nhận được: "${content}"`);
    console.log(`================================\n`);

    return res.status(200).json({ 
        success: true, 
        message: 'Server đã nhận tin nhắn', 
        receivedData: content 
    });
});

app.listen(PORT, () => {
    console.log(`================================`);
    console.log(` Server chạy tại: http://localhost:${PORT}`);
    console.log(` Để demo qua mạng (và chạy HTTPS), hãy sử dụng lệnh:`);
    console.log(` ngrok http ${PORT}`);
    console.log(`================================`);
});

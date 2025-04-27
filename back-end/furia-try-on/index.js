import express from "express";
import axios from "axios";
import fs from "fs";
import path from "path";
import { fileURLToPath } from "url";
import { dirname } from "path";
import dotenv from "dotenv";
import * as cheerio from "cheerio";

dotenv.config();

const app = express();
const PORT = 3000;
app.use(express.json());

const apiKey = process.env.API_KEY;

const URL = 'https://www.furia.gg/produtos';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

function imageFileToBase64(imagePath) {
  const imageData = fs.readFileSync(path.resolve(__dirname, imagePath));
  return Buffer.from(imageData).toString("base64");
}

async function imageUrlToBase64(imageUrl) {
  const response = await axios.get(imageUrl, { responseType: "arraybuffer" });
  return Buffer.from(response.data, "binary").toString("base64");
}

app.get('/products', async (req, res) => {
  try {
    const { data: html } = await axios.get(URL);
    const $ = cheerio.load(html);
    const products = [];

    $('.inner__spot').each((_, el) => {
      const name = $(el).find('.spot__name h3').text().trim();
      const link = $(el).find('.spot__image a').attr('href');
      const image = 
        $(el).find('.spot-main-image img').attr('data-srcset') ||
        $(el).find('.spot-main-image img').attr('data-src') ||
        $(el).find('.spot-main-image img').attr('src');

      if (name && image && link) {
        products.push({ name, image, link });
      }
    });

    res.json(products);
  } catch (err) {
    console.error('Erro ao coletar produtos:', err);
    res.status(500).json({ error: 'Erro ao obter produtos' });
  }
});

app.post("/try-on", async (req, res) => {
  try {
    const { cloth_image, pose_image } = req.body;

    const modelImageBase64 = pose_image.startsWith("http")
      ? await imageUrlToBase64(pose_image)
      : imageFileToBase64(pose_image);

    const clothImageBase64 = cloth_image.startsWith("http")
      ? await imageUrlToBase64(cloth_image)
      : imageFileToBase64(cloth_image);

    const data = {
      model_image: modelImageBase64,
      cloth_image: clothImageBase64,
      category: "Upper body",
      num_inference_steps: 20,
      guidance_scale: 2,
      seed: 12467,
      base64: false,
    };

    const response = await axios.post("https://api.segmind.com/v1/try-on-diffusion", data, {
      headers: {
        "x-api-key": apiKey,
      },
      responseType: "arraybuffer",
    });

    const imageBuffer = response.data;
    const base64Image = Buffer.from(imageBuffer, "binary").toString("base64");

    res.json({ image: base64Image });
  } catch (error) {
    console.error("Erro:", error.response?.data || error.message);
    res.status(500).json({ error: "Erro ao gerar imagem com try-on." });
  }
});

app.listen(PORT, () => {
  console.log(`Servidor rodando na porta ${PORT}`);
});

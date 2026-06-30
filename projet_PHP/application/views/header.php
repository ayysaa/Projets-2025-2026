<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $title; ?></title>
    <style>
        :root {
            --bg-color: #f8f9fa;
            --card-bg: #ffffff;
            --text-color: #2b2b2b;
            --primary: #2c3e50;
            --accent: #2980b9;
            --danger: #c0392b;
            --border-color: #e2e8f0;
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
            background-color: var(--bg-color);
            color: var(--text-color);
            margin: 0;
            padding: 0;
        }
        header {
            background-color: var(--primary);
            color: #ffffff;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        header h1 {
            margin: 0;
            font-size: 22px;
        }
        nav a {
            color: #ffffff;
            text-decoration: none;
            margin-left: 20px;
            font-size: 15px;
        }
        nav a:hover {
            text-decoration: underline;
        }
        main {
            max-width: 1100px;
            margin: 30px auto;
            padding: 0 20px;
        }
        .card {
            background: var(--card-bg);
            padding: 25px;
            border-radius: 6px;
            border: 1px solid var(--border-color);
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid var(--border-color);
        }
        th {
            background-color: #f1f5f9;
            color: var(--primary);
        }
        .btn {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 4px;
            text-decoration: none;
            font-size: 14px;
            border: none;
            cursor: pointer;
        }
        .btn-primary { background-color: var(--accent); color: white; }
        .btn-danger { background-color: var(--danger); color: white; }
        .btn-secondary { background-color: #7f8c8d; color: white; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-control { width: 100%; padding: 8px; border: 1px solid var(--border-color); border-radius: 4px; box-sizing: border-box; }
        .flex-container { display: flex; gap: 40px; }
        .flex-child { flex: 1; }
        ul { padding-left: 20px; }
        ul li { margin-bottom: 8px; }
    </style>
</head>
<body>
    <header>
        <h1>🎮 GameList</h1>
        <nav>
            <a href="<?php echo base_url('jeux'); ?>">Liste des jeux</a>
            <a href="<?php echo base_url('jeux/categories_genres'); ?>">Catégories & Genres</a>
        </nav>
    </header>
    <main>
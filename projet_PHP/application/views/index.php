<div class="card">
    <h2>Parcourir la base de données</h2>
    <div class="flex-container">
        <div class="flex-child">
            <h3>📁 Catégories</h3>
            <ul>
                <?php foreach ($categories as $cat): ?>
                    <li>
                        <a href="<?php echo base_url('jeux/categorie/'.$cat['id']); ?>">
                            <?php echo html_escape($cat['description']); ?>
                        </a>
                    </li>
                <?php endforeach; ?>
            </ul>
        </div>

        <div class="flex-child">
            <h3>🎭 Genres</h3>
            <ul>
                <?php foreach ($genres as $genre): ?>
                    <li>
                        <a href="<?php echo base_url('jeux/genre/'.$genre['id']); ?>">
                            <?php echo html_escape($genre['description']); ?>
                        </a>
                    </li>
                <?php endforeach; ?>
            </ul>
        </div>
    </div>
</div>
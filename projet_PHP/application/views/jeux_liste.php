<div class="card">
    <h2>Liste des Jeux Vidéos</h2>

    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <form method="get" action="<?php echo base_url('jeux'); ?>" style="display: flex; gap: 10px; margin: 0;">
            <input type="text" name="search" value="<?php echo html_escape($search ?? ''); ?>" placeholder="Rechercher par titre..." class="form-control" style="width: 250px;">
            <button type="submit" class="btn btn-primary">Rechercher</button>
            <?php if (!empty($search)): ?>
                <a href="<?php echo base_url('jeux'); ?>" class="btn btn-secondary">Réinitialiser</a>
            <?php endif; ?>
        </form>

        <a href="<?php echo base_url('jeux/creer'); ?>" class="btn btn-primary">+ Ajouter un jeu</a>
    </div>

    <p style="font-size: 14px;">
        Trier par : 
        <a href="<?php echo base_url('jeux/index/name' . (!empty($search) ? '?search='.urlencode($search) : '')); ?>" style="<?php echo $tri_actuel == 'name' ? 'font-weight:bold;' : ''; ?>">Titre</a> | 
        <a href="<?php echo base_url('jeux/index/releaseYear' . (!empty($search) ? '?search='.urlencode($search) : '')); ?>" style="<?php echo $tri_actuel == 'releaseYear' ? 'font-weight:bold;' : ''; ?>">Année de sortie</a>
    </p>

    <table>
        <thead>
            <tr>
                <th>Titre</th>
                <th>Année</th>
                <th>Note Metacritic</th>
                <th style="text-align: right;">Actions</th>
            </tr>
        </thead>
        <tbody>
            <?php if (!empty($games)): ?>
                <?php foreach ($games as $g): ?>
                <tr>
                    <td><strong><?php echo html_escape($g['name']); ?></strong></td>
                    <td><?php echo html_escape($g['releaseYear']); ?></td>
                    <td><?php echo html_escape($g['metacritic'] ?? 'N/A'); ?> / 100</td>
                    <td style="text-align: right;">
                        <a href="<?php echo base_url('jeux/voir/'.$g['id']); ?>" class="btn btn-secondary" style="padding: 4px 8px; font-size: 13px;">Voir</a>
                        <a href="<?php echo base_url('jeux/editer/'.$g['id']); ?>" class="btn btn-primary" style="padding: 4px 8px; font-size: 13px; background-color: #d35400;">Modifier</a>
                        <a href="<?php echo base_url('jeux/supprimer/'.$g['id']); ?>" class="btn btn-danger" style="padding: 4px 8px; font-size: 13px;" onclick="return confirm('Confirmer la suppression de ce jeu ?');">Supprimer</a>
                    </td>
                </tr>
                <?php endforeach; ?>
            <?php else: ?>
                <tr>
                    <td colspan="4" style="text-align: center; color: #7f8c8d;">Aucun jeu ne correspond aux critères.</td>
                </tr>
            <?php endif; ?>
        </tbody>
    </table>
</div>
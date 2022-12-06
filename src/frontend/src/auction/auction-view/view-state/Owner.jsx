import React from 'react';
import { Button, Flex } from '@adobe/react-spectrum';
import { useNavigate } from 'react-router-dom';

export const Owner = ({ auctionId, otherProps }) => {
    const navigate = useNavigate();

    return (
        <Flex {...otherProps}>
            <Button
                alignSelf="center"
                variant="cta"
                onPress={() => navigate(`/auction/${auctionId}/edit`)}
                {...otherProps}
            >
                Edit
            </Button>
        </Flex>
    );
};

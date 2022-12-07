import React, { useCallback, useState } from 'react';
import {
    Button,
    Content,
    Dialog,
    Flex,
    Heading,
    DialogTrigger,
} from '@adobe/react-spectrum';
import { usePostWithToast } from '../../../http-query/use-post-with-toast';
import { StarRating } from '../../../star-rating/StarRating';
import { useEffect } from 'react';
import toast from 'react-hot-toast';
import { Toast } from '../../../toast/Toast';

export const WonBid = ({ auction, refresh, ...otherProps }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [rating, setRating] = useState(auction?.seller?.yourRating || 0);

    const isValid = useCallback(() => rating > 0, [rating]);

    const updateRating = usePostWithToast(
        `/auctions/${auction.id}/updateRating`,
        null,
        [auction],
        { message: 'Rating updated!' },
        { message: 'Failed to update rating!' },
        () => {
            setIsOpen(false);
            refresh();
        },
    );

    useEffect(() => {
        const message = auction.winner.hasPaid
            ? `Thank you for finishing the transaction with ${auction.seller.alias}`
            : `Please make sure to collect your item from ${auction.seller.alias}`;
        toast.custom((t) => (
            <Toast
                message={message}
                type={auction.winner.hasPaid ? 'positive' : 'notice'}
                dismissFn={() => toast.remove(t.id)}
            />
        ));
    }, []);

    return (
        <DialogTrigger type="popover" isOpen={isOpen} onOpenChange={setIsOpen}>
            <Button alignSelf="center" variant="cta" {...otherProps}>
                {rating > 0 ? 'Update Rating' : 'Rate Seller'}
            </Button>
            <Dialog size="M">
                <Heading>Rate {auction?.seller?.alias}</Heading>
                <Content>
                    <Flex
                        direction="rows"
                        justifyContent="space-between"
                        alignItems="end"
                        marginTop="size-100"
                        gap="size-50"
                    >
                        <StarRating rating={rating} onPress={setRating} />
                        <Button
                            width="size-2000"
                            variant="cta"
                            onPress={() =>
                                updateRating({ rating, auctionId: auction.id })
                            }
                            isDisabled={!isValid()}
                        >
                            Rate
                        </Button>
                    </Flex>
                </Content>
            </Dialog>
        </DialogTrigger>
    );
};
